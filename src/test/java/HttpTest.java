import bottle.util.HttpUtil;

import java.io.File;

/**
 * @Author: leeping
 * @Date: 2020/3/10 10:16
 */
public class HttpTest {
    public static void main(String[] args) {
        String url = "http://fs.onek56.com:8877/print/40676E556B55CFB713E28CA0AA57F708/1583808464359.docx";

        File local = new File("C:\\IDEAWORK\\erp-client\\out\\production\\printFile\\1.docx");
        new HttpUtil.Request(url, null)
                .setDownloadFileLoc(local).download().execute();
    }
}
