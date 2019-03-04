package bottle.backup.slice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/24.
 */
public class SliceScrollResult {
    public List<SliceMapper> list_same = new ArrayList<>();
    public List<SliceMapper> list_diff = new ArrayList<>();

    public int getDifferentSize(){
        return list_diff.size();
    }

    public String getDifferentBlockSequence() {
        StringBuilder sb = new StringBuilder();
        for (SliceMapper mapper : list_diff){
                sb.append(mapper.position).append("-").append(mapper.length).append("#");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public String getSameBlockSequence() {
        StringBuilder sb = new StringBuilder();
        for (SliceMapper mapper : list_same){
            sb.append(mapper.position).append("-").append(mapper.length).append("-").append(mapper.sliceInfo.position).append("#");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public int getSameSize() {
        return list_same.size();
    }
}
