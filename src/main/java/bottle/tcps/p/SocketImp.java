package bottle.tcps.p;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by user on 2017/7/8.
 */
public interface SocketImp {

    /**
     * 获取socket
     */
     AsynchronousSocketChannel getSocket();
    /**
     * 是否存活
     */
     boolean isAlive();
    /**
     * 获取通讯实现对象
     */
     FtcTcpActions getAction();
    /**
     * 设置通讯实现对象
     */
    void setAction(FtcTcpActions action);
    /**
     * 关闭
     */
    void close();

    /**
     * 获取会话
     */
    Session getSession();

}
