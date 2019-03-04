import bottle.backup.server.FtcBackupServer;

import java.io.IOException;

public class FtcServerTest {
    public static void main(String[] args) throws IOException {
        FtcBackupServer ftcBackupServer = new FtcBackupServer("D:\\ftcServer\\s","192.168.1.145",7777,0,0);
//        ftcBackupServer.getClient().watchDirectory(true); //监听目录变化
//        ftcBackupServer.getClient().addServerAddress();
        ftcBackupServer.setCallback( file -> System.out.println(("收到文件 - "+ file)) );
        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
