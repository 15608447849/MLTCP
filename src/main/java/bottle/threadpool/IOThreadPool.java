package bottle.threadpool;

import java.util.concurrent.*;


public class IOThreadPool  extends Thread implements IThreadPool {

    private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private ThreadPoolExecutor executor = this.createIoExecutor(1000);
    private boolean isLoop = true;

    public IOThreadPool() {
        this.setName("IO线程池保留线程-" + this.getId());
        this.start();
    }

    private ThreadPoolExecutor createIoExecutor(int capacity) {
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                capacity / 2,
                30L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(capacity), (r) -> {
            Thread thread = new Thread(r);
            thread.setName("t-pio#-" + thread.getId());
            return thread;
        }, (r, executor) -> {
            this.queue.offer(r);
            synchronized(this.queue) {
                this.queue.notify();
            }
        });
    }

    public void run() {
        while(this.isLoop) {
            try {
                Runnable runnable = this.queue.poll();
                if (runnable == null) {
                    synchronized(this.queue) {
                        try {
                            this.queue.wait();
                        } catch (InterruptedException var5) {
                            var5.printStackTrace();
                        }
                    }
                } else {
                    runnable.run();
                }

            } catch (Exception var7) {
                var7.printStackTrace();
            }
        }

    }

    public void post(Runnable runnable) {
        this.executor.execute(runnable);
    }

    public void close() {
        this.isLoop = false;
        synchronized(this.queue) {
            this.queue.notify();
        }
        if (this.executor != null) {
            this.executor.shutdownNow();
        }
    }
}
