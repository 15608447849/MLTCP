package bottle.backup.imps;

/**
 * Created by user on 2017/11/23.
 */
public class Protocol {
    public static final String C_FILE_BACKUP_QUEST="客户端文件同步请求";
    public static final String S_FILE_BACKUP_QUEST_ACK="服务端文件同步请求回执";
    public static final String C_FILE_BACKUP_TRS_START="客户端文件传输到服务端-开始";
    public static final String S_FILE_BACKUP_NOTIFY_STREAM="服务端通知客户端传输流";
    public static final String C_FILE_BACKUP_TRS_END="客户端文件传输到服务端-结束";
    public static final String S_FILE_BACKUP_TRS_OVER="服务端文件传输事件完成";
    public static final String C_FILE_LIST_VERIFY_QUEST="客户端文件列表确认请求";
    public static final String S_FILE_LIST_VERIFY_ACK="服务端文件列表确认回执";
}
