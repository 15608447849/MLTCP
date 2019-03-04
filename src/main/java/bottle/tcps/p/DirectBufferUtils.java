package bottle.tcps.p;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

public class DirectBufferUtils {
    public static final int PROTOCOL_BIT_SIZE = 8;
    public static final int BUFFER_BLOCK_SIZE = 16 * 1024 * 1024; // 4M jvm堆外内存

    public static ByteBuffer createByteBuffer(int size){
        return ByteBuffer.allocateDirect(size);//申请堆外内存
//        return ByteBuffer.allocate(size);
    }

    public static void clean(final ByteBuffer byteBuffer) {
        byteBuffer.clear();
        if (byteBuffer.isDirect()) {
//            (byteBuffer);
            ((DirectBuffer)byteBuffer).cleaner().clean();
        }
    }











}
