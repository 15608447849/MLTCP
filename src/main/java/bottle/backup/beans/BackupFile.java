package bottle.backup.beans;


import bottle.util.EncryptUtils;
import bottle.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by lzp on 2017/11/24.
 */
public class BackupFile {


    private String dirs;
    private String relPath;
    private String fileName;
    private String fullPath;
    private String md5;
    private long flen;

    public BackupFile(String dirs, String path) {
        this.dirs = FileUtils.replaceFileSeparatorAndCheck(dirs,null,FileUtils.SEPARATOR);
        path = FileUtils.SEPARATOR+FileUtils.replaceFileSeparatorAndCheck(path,FileUtils.SEPARATOR,null);
        this.relPath = path.substring(0,path.lastIndexOf(FileUtils.SEPARATOR)+1);
        this.fileName = path.substring(path.lastIndexOf(FileUtils.SEPARATOR)+1);
        this.fullPath = dirs+ relPath +fileName;
        File f = new File(fullPath);
        this.flen = f.length();
        try {
            this.md5 = EncryptUtils.getFileMd5ByString(f);
        } catch (Exception e) {
            this.md5 = "";
        }
    }

    public String getDirs() {
        return dirs;
    }

    public String getRelPath() {
        return relPath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFullPath() { return fullPath; }

    public long getFileLength() {
        return new File(getFullPath()).length();
    }

    public String getMd5() {
        return md5;
    }

    @Override
    public String toString() {
        return getFullPath();
    }
}
