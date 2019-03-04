package bottle.backup.imps;


import bottle.threadpool.IOThreadPool;
import bottle.threadpool.IThreadPool;
import bottle.threadpool.MThreadPool;
import bottle.util.FileUtils;

import java.io.File;

/**
 * Created by user on 2017/11/23.
 */
public abstract class FtcBackAbs {
    //线程池
    protected final IThreadPool pool = new IOThreadPool();

    //目录
    protected final String directory ;
    public FtcBackAbs(String directory) {
        this.directory = FileUtils.replaceFileSeparatorAndCheck(directory,null, File.pathSeparator);
    }

    public String getDirectory() {
        return directory;
    }
}
