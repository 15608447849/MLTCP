package bottle.util;

import bottle.tcps.p.FtcTcpActionsAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Author: leeping
 * @Date: 2019/3/18 10:57
 */
public class Log4j {
    public final static Logger logger = LogManager.getLogger();


    public static void info(Object obj){
        logger.info(obj);
    }

    public static void error(Object obj){
        logger.error(obj);
    }
    public static void error(String message, Throwable t){
        logger.error(message,t);
    }
}
