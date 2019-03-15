package bottle.backup.slice;

import bottle.util.EncryptUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Created by user on 2017/11/24.
 */
public class SliceUtil {

    private static void close(RandomAccessFile r){
        try {
            if (r!=null) r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int sliceSizeConvert(long fileSize){
        //按照  (512 / 100*1024*2) >> (0.025) 的比例换算 2.5%
        int sliceSize = (int) (fileSize *  0.025);
        return sliceSize>0 ? sliceSize : 512;
    }

    /**
     * 对文件分片,返回分片信息
     */
    public static ArrayList<SliceInfo> fileSliceInfoList(File file, int sliceSize){
        long fileSize = file.length();
        long sliceSum = fileSize / sliceSize;
        int mod = (int) (fileSize % sliceSize);
        if (mod>0){
            sliceSum+=1;
        }
        RandomAccessFile randomAccessFile = null;
        try{
            randomAccessFile = new RandomAccessFile(file,"r");
            ArrayList<SliceInfo> sliceList = new ArrayList<>();
            int len;
            long position = 0;
            byte[] buffer  = new byte[sliceSize];
            SliceInfo sliceInfo;
            for(int i = 0 ; i < sliceSum;i++ ){
                len = (int) Math.min(sliceSize,(randomAccessFile.length()-position));

                randomAccessFile.seek(position);
                randomAccessFile.read(buffer,0,len);
//                ("数据分片: "+ new String(buffer,0,len)+" , "+ " - "+ len);
                sliceInfo = new SliceInfo();
                sliceInfo.position = position;
                sliceInfo.length = len;
                sliceInfo.adler32Hex = EncryptUtils.adler32Hex(buffer,0,len);
                sliceInfo.md5Hex = EncryptUtils.getBytesMd5ByString(buffer,0,len);
                sliceList.add(sliceInfo);
                position += sliceSize;
            }
            return sliceList;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            close(randomAccessFile);
        }
        return null;
    }

    //对分片数据 分组 ,方便对比查询
    public static Hashtable<String,LinkedList<SliceInfo>> sliceInfoToTable(ArrayList<SliceInfo> sliceInfoList) {
        Hashtable<String,LinkedList<SliceInfo>> table = new Hashtable<>();
        for (SliceInfo sliceBean:sliceInfoList){
            LinkedList list = table.get(sliceBean.adler32Hex);
            if (list==null){list  = new LinkedList();}
            list.add(sliceBean);
            table.put(sliceBean.adler32Hex,list);
        }
        return table;
    }


    /**
     * 滚动检测
     */
    public static SliceScrollResult scrollCheck(Hashtable<String, LinkedList<SliceInfo>> table, File file, int sliceSize) {
        RandomAccessFile randomAccessFile = null;
        try{
            randomAccessFile = new RandomAccessFile(file, "r");
            long fileLength = randomAccessFile.length();
            int move = (int)(fileLength*0.01);
            int len = 0;
            long position = 0;
            boolean moveBlock = false;
            byte[] buf = new byte[sliceSize];
            String adler32Hex;
            Iterator<Map.Entry<String,LinkedList<SliceInfo>>> iterator;
            Map.Entry<String,LinkedList<SliceInfo>> entry;
            String md5;
            LinkedList list;
            Iterator<SliceInfo> it ;
            SliceInfo sliceInfo;
            SliceMapper mapper_same;

            SliceScrollResult result = new SliceScrollResult();
            while(true){

                moveBlock = false;
                len = (int) Math.min(sliceSize,(fileLength-position));
                if (len<1) {
                    break;
                }

                randomAccessFile.seek(position);

                randomAccessFile.read(buf,0,len);

                adler32Hex = EncryptUtils.adler32Hex(buf,0,len);

                iterator = table.entrySet().iterator();
                while (iterator.hasNext()){
                    entry = iterator.next();
                    if (entry.getKey().equalsIgnoreCase(adler32Hex)){
                        md5 = EncryptUtils.getBytesMd5ByString(buf,0,len);
                        list = entry.getValue();
                        if (list.size()>0){
                            it = list.iterator();
                            while (it.hasNext()){
                                sliceInfo = it.next();
                                if (sliceInfo.md5Hex.equals(md5)){
                                    mapper_same = new SliceMapper();
                                    mapper_same.type = 0;
                                    mapper_same.position = position;
                                    mapper_same.length = sliceInfo.length;
                                    mapper_same.sliceInfo = sliceInfo;
                                    result.list_same.add(mapper_same);
                                    moveBlock = true;
                                    it.remove();
                                    break;
                                }
                            }
                        }else{
                            iterator.remove();
                        }
                    }
                    if (moveBlock) break;
                }

                if (moveBlock){
//                   ("移动块 : ",(Math.min((int)(fileLength-position),sliceSize)<sliceSize)?(int)(fileLength-position):sliceSize);
                    position+=sliceSize;//向后移动一块数据

                }else{
//                   ("移动格子 : ",(Math.min((int)(fileLength-position),move)<move)?1:move);
                    position+=move;  //向后偏移文件大小的1% 字节

                }
//               ("滚动检测: ", position ," , " +fileLength," , " ,NumberFormat.getInstance().format((double)position/(double)fileLength * 100),"%");
                if (position>fileLength) break;
//               ("滚动检测: ", position ," , " +fileLength," , " ,NumberFormat.getInstance().format((double)position/(double)fileLength * 100),"%");
            }

            //根据相同的 确定不同的坐标
            SliceMapper mapper_diff;
            long recodePosition = 0;
            if (result.getSameSize()>0){
                for (SliceMapper same : result.list_same){
                    if (recodePosition<same.position){
                        len = (int) (same.position - recodePosition);
                        mapper_diff = new SliceMapper();
                        mapper_diff.type = 1;
                        mapper_diff.position = recodePosition;
                        mapper_diff.length = len;
                        result.list_diff.add(mapper_diff);

                    }
                     recodePosition = same.position + same.length;
                }

                if (recodePosition!=fileLength){
                    len = (int) (fileLength - recodePosition);
                    mapper_diff = new SliceMapper();
                    mapper_diff.type = 1;
                    mapper_diff.position = recodePosition;
                    mapper_diff.length = len;
                    result.list_diff.add(mapper_diff);
                }
            }

           return result;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            close(randomAccessFile);
        }
        return null;
    }
}
