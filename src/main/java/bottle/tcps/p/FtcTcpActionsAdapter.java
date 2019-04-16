package bottle.tcps.p;

import bottle.util.Log4j;

/**
 * Created by user on 2017/11/23.
 */
public abstract class FtcTcpActionsAdapter implements FtcTcpActions{

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
            Log4j.error("连接错误 " + session.getSocket(),throwable);
//            (session.getSocket(),throwable.getCause());
        }
        if (e!=null){
//            e.printStackTrace();
            Log4j.error("连接错误 " + session.getSocket(),e);
//            (session.getSocket(),e.getCause());
        }
    }
}
