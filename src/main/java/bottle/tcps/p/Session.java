package bottle.tcps.p;



import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * Created by user on 2017/7/8.
 * 通讯会话
 */
public abstract class Session implements CompletionHandler<Integer, ByteBuffer>{

    private final FtcTcpAioManager ftcTcpManager;
    private final SocketImp socketImp;


    private final BufferRecThread recBuffer = new BufferRecThread(this);//接受数据存储
    private final SessionOperation operation =  new SessionOperation(this);
    private final BufferRecContentHandle recContentHandle = new BufferRecContentHandle(this); //接收内容处理者
    private final BufferSendThread sendBuffer = new BufferSendThread(this);

    public Session(FtcTcpAioManager manager, SocketImp connect) {
        this.ftcTcpManager = manager;
        this.socketImp = connect;
    }

    //系统读取到信息 回调到这里
    @Override
    public void completed(Integer integer, ByteBuffer buffer) {
        if (buffer!=null && socketImp.isAlive()) {
            if (integer == -1){
                //一个客户端 连接异常
                socketImp.getAction().error(this,null, new SocketException("socket connect is closed."));
                socketImp.getAction().connectClosed(this);
                return;
            }
            if (integer > 0){
                recBuffer.storeBuffer(buffer); //存入数据
            }
        }
        read();
    }

    @Override
    public void failed(Throwable throwable, ByteBuffer buffer) {
        //读取数据异常
        socketImp.getAction().error(this,throwable,null);
        socketImp.getAction().connectClosed(this);
    }

    /**
     * 读取数据(异步)
     */
    public void read(){
            ByteBuffer buffer = operation.getReadBufferBySystemTcpStack();
            if (buffer!=null){
                if (socketImp.isAlive()){
                    socketImp.getSocket().read(buffer, buffer,this);//系统从管道读取数据
                }else{
                    socketImp.getAction().error(this,null, new SocketException("socket connect is closed."));
                }
            }
    }

    /**
     * 发送数据(同步-堵塞)
     */
    public void send(ByteBuffer buffer) {
                if (buffer != null && socketImp.isAlive()){
                  buffer.flip();
                  sendBuffer.putBuf(buffer);
                }
    }

    public void clear(){
        //清理发送到缓冲区的数据
        sendBuffer.close();
        //清理从缓冲区接收到数据
        recBuffer.close();
        //清理发送/接受时 利用的buffer
        recContentHandle.clear();
        //清理接受数据处理剩余剩余及拼接等的数据
    }

    public void close(){
        //关闭管道
        socketImp.close();
        //清理
        clear();
    }



    public SessionOperation getOperation(){return operation;}

    public AsynchronousSocketChannel getSocket(){
        return socketImp.getSocket();
    }

    public SocketImp getSocketImp(){ return socketImp; }

    public BufferRecThread getStore(){return recBuffer;}

    public FtcTcpAioManager getFtcTcpAioManager(){
        return ftcTcpManager;
    }

    public void handlerRecContent(byte[] bytes) {
        recContentHandle.handlerBuffer(bytes);
    }


}
