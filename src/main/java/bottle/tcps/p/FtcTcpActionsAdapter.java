package bottle.tcps.p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by user on 2017/11/23.
 */
public abstract class FtcTcpActionsAdapter implements FtcTcpActions{
    private static final Logger logger = LogManager.getLogger(FtcTcpActionsAdapter.class.getName());

    @Override
    public void connectSucceed(Session session) {

    }

    @Override
    public void receiveString(Session session, String message) {

    }

    @Override
    public void receiveBytes(Session session, byte[] bytes) {

    }
    //连接关闭
    @Override
    public void connectClosed(Session session) {
        if (session!=null && session.getSocketImp()!=null && session.getSocketImp().isAlive()){
            session.close();
        }
    }
    //客户端 - 连接失败
    @Override
    public void connectFail(Session session) {

    }

    @Override
    public void error(Session session, Throwable throwable, Exception e) {

        if (throwable!=null){
//            throwable.printStackTrace();
            logger.error("连接错误 " + session.getSocket(),throwable);
//            (session.getSocket(),throwable.getCause());
        }
        if (e!=null){
//            e.printStackTrace();
            logger.error("连接错误 " + session.getSocket(),e);
//            (session.getSocket(),e.getCause());
        }
    }
}
