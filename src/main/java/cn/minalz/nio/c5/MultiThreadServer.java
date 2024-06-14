package cn.minalz.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * 多线程选择器
 * @author zhouwei
 * @date 2024/6/14 14:42
 */
@Slf4j
public class MultiThreadServer {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        WorkerEventLoop[] workers = new WorkerEventLoop[2];
        for (int i = 0; i < 2; i++) {
            workers[i] = new WorkerEventLoop(i);
        }
        AtomicInteger index = new AtomicInteger();

        while (true) {
            log.info("boss run...");
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info("connected: {}", sc.getRemoteAddress());
                    log.info("before register: {}", sc.getRemoteAddress());
                    workers[index.getAndIncrement() % workers.length].register(sc);
                    log.info("after register: {}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class WorkerEventLoop implements Runnable {

        private Selector selector;
        private volatile boolean start = false;
        private final int index;

        private final ConcurrentLinkedDeque<Runnable> tasks = new ConcurrentLinkedDeque<>();

        public WorkerEventLoop(int index) {
            this.index = index;
        }

        public void register(SocketChannel sc) throws IOException {
            log.info("worker begin register");
            if (!start) {
                selector = Selector.open();
                new Thread(this, "worker-" + index).start();
                start = true;
            }
            // 将sc.register(selector, SelectionKey.OP_READ); 放在selector.wakeup()之下也是可以的
            /*tasks.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });*/
            // 唤醒 select 方法
            selector.wakeup();
            sc.register(selector, SelectionKey.OP_READ);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    log.info("work run...");
                    selector.select();
                    Runnable poll = tasks.poll();
                    if (poll != null) {
                        poll.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            try {
                                int read = sc.read(buffer);
                                if (read == -1) {
                                    key.cancel();
                                } else {
                                    buffer.flip();
                                    log.info("message: {}", sc.getRemoteAddress());
                                    debugAll(buffer);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                key.cancel();
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
