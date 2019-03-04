import bottle.backup.client.FtcBackupClient;

import java.io.File;
import java.net.InetSocketAddress;

public class FtcClientTest {
    public static void main(String[] args) {
        FtcBackupClient client  = new FtcBackupClient("D:\\ftcServer\\c",
                64,
                2000);
        client.addServerAddress(new InetSocketAddress("192.168.1.145",7777));
//        client.ergodicDirectory();
//        client.addBackupFile(new File("D:\\ftcServer\\c\\DiskGenius\\DiskGenius.exe"));
//        client.addBackupFile(new File("D:\\ftcServer\\c\\DiskGenius\\avcodec-54.dll"));
        File file = new File("D:\\ftcServer\\c\\DiskGenius");
        for (File f : file.listFiles()){
            if (f.isFile()) client.addBackupFile(f);
        }
        try {
            Thread.sleep(10000 * 60 * 60 * 24);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
