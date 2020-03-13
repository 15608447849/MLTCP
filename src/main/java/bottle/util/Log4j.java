package bottle.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: leeping
 * @Date: 2019/8/16 17:01
 */
public class Log4j {

    public interface PrintCallback{
        void callback(Object message);
    }
    private static PrintCallback callback;

    public static void setCallback(PrintCallback callback) {
        Log4j.callback = callback;
    }

    public final static Logger logger = LogManager.getLogger();

    public static void debug(Object obj){
        logger.debug(obj);
        if (callback!=null) callback.callback(obj);
    }

    public static void info(Object obj){
        logger.info(obj);
        if (callback!=null) callback.callback(obj);
    }

    public static void info(String... str){
        if(str==null) return;
        StringBuilder sb = new StringBuilder();
        for (String s : str){
            sb.append(s).append(" ");
        }
        logger.info(sb.toString());
        if (callback!=null) callback.callback(sb.toString());
    }

    public static void error(Object obj){
        logger.error(obj);
        if (callback!=null) callback.callback(obj);
    }

    public static void error(String message, Throwable t){
        logger.error(message,t);
        if (callback!=null) callback.callback(t);
    }

    public static void trace(Object obj){
        logger.trace(obj);
        if (callback!=null) callback.callback(obj);
    }

    public static void warn(Object obj){
        logger.warn(obj);
        if (callback!=null) callback.callback(obj);
    }

    public static void fatal(Object obj){
        logger.fatal(obj);
        if (callback!=null) callback.callback(obj);
    }
}
