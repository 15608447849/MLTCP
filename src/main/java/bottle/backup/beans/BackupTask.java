package bottle.backup.beans;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackupTask {

    private int type =  -1;

    private BackupFile backupFile;

    private InetSocketAddress serverAddress;

    private int loopCount = 0;

    public BackupTask( InetSocketAddress serverAddress,BackupFile backupFile) {
        type = 1;
        this.backupFile = backupFile;
        this.serverAddress = serverAddress;
    }

    private List<BackupFile> backupFileList;

    public BackupTask(InetSocketAddress serverAddress, List<BackupFile> backupFileList) {
        type = 2;
        this.serverAddress = serverAddress;
        this.backupFileList = new ArrayList<>(backupFileList);
    }

    public int getType() {
        return type;
    }

    public BackupFile getBackupFile() {
        return backupFile;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public List<BackupFile> getBackupFileList() {
        return backupFileList;
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void incLoopCount() {
        this.loopCount++;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (type == 1 ) stringBuilder.append(backupFile.getFullPath());
        else if (type == 2){
            for (BackupFile file : backupFileList){
                stringBuilder.append(file.getFullPath()+",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);
            stringBuilder.append("\n");
        }
        stringBuilder.append(" >> ").append(serverAddress);
        return stringBuilder.toString();
    }
}
