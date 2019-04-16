package bottle.tcps.p;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Created by user on 2017/7/8.
 * 存在 内容缓存区buf
 */
class BufferRecThread extends Thread {


    private volatile boolean isFlag = true;

    private Session session;

    public BufferRecThread(Session session) {
        this.session =session;
        this.start();
    }

    /**
     * 系统接收到的缓冲区 包数据
     */
    private final BlockingQueue<byte[]> receiveBufferQueue = new LinkedBlockingQueue<>();

    /**
     * 存储系统读取到的 数据包
     * @param buffer
     */
    public void storeBuffer(ByteBuffer buffer) {
        try {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            buffer.clear();
            receiveBufferQueue.put(bytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }











    //间隔读取缓冲区数据
    @Override
    public void run() {
        while (isFlag){
            try {
                byte[] bytes = receiveBufferQueue.take();
                session.handlerRecContent(bytes);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        receiveBufferQueue.clear();
    }

    public void close() {
        isFlag = false;
    }

}
