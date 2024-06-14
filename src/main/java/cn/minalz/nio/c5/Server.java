package cn.minalz.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * 使用 selector 创建服务端
 *
 * @author zhouwei
 * @date 2024/6/14 10:30
 */
@Slf4j
public class Server {

    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                // 把这条完整消息存入新的ByteBuffer中
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                // 从source读 向target写
                source.limit(i + 1);
                target.put(source);
                debugAll(target);
                source.limit(oldLimit);
            }
        }
        source.compact();
    }

    public static void main(String[] args) throws IOException {
        // 1.创建selector 管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        // 2.建立selector和channel的联系（注册）
        // 2.1 SelectionKey 就是将来事件发生后 通过它可以知道事件和哪个channel事件相关
        SelectionKey sscKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
        log.info("sscKey: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            // 3. select方法 没有事件发生的时候 线程阻塞 有事件发生 线程才会回复运行
            // 3.1 在事件未处理时 它不会阻塞 事件发生后要么处理 要么取消 不能置之不理
            selector.select();
            // 获取所有事件 selectedKeys 内部包含了所有发生的事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 遍历所有事件 逐一处理
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
//                // 需要将事件移除
                iterator.remove();
                log.info("key: {}", key);
                // 判断事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    // 必须处理
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    // 将一个ByteBuffer 作为附件关联到selectionKey上
                    SelectionKey scKey = sc.register(selector, SelectionKey.OP_READ, buffer);
                    log.info("scKey: {}", scKey);
                } else if (key.isReadable()) {
                    try {
                        // 拿到触发事件的channel
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 获取selectionKey上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 如果是正常断开 read方法的返回值是-1
                        int read = channel.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            split(buffer);
                            // 需要扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 因为客户端断开了 因此需要将key取消（从selector的keys集合中真正删除key）
                        key.cancel();
                    }
                }
            }
        }
    }
}
