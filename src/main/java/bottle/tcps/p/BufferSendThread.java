package bottle.tcps.p;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class BufferSendThread extends Thread{
    /**
     * 需要发送到的缓冲区包数据
     */
    private final BlockingQueue<ByteBuffer> sendBufferQueue = new LinkedBlockingQueue<>();

    private final Session session;

    private volatile boolean isFlag = true;

    public BufferSendThread(Session session) {
        this.session = session;
        this.setName("buf-send-t"+this.getId());
        this.start();
    }


    public void putBuf(ByteBuffer buffer) {

        try {
            sendBufferQueue.put(buffer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (isFlag){
            try {
                ByteBuffer buffer = sendBufferQueue.take();
                SocketImp socketImp = session.getSocketImp();
                try {
                    Future<Integer> future =  socketImp.getSocket().write(buffer); //发送消息到管道
                    while(!future.isDone());
                } catch (Exception e) {
                    //发送数据异常
                    socketImp.getAction().error(session,null,e);
                    socketImp.getAction().connectClosed(session);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        sendBufferQueue.clear();
    }

    public void close() {
        isFlag = false;
    }
}
