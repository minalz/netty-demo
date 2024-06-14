package cn.minalz.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * 非阻塞模式服务端
 * @author zhouwei
 * @date 2024/6/11 15:18
 */
@Slf4j
public class NioServerDemo {

    public static void main(String[] args) throws IOException {
        // 1. 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 2.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 2.1 非阻塞模式
        ssc.configureBlocking(false);
        // 3.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        // 4.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 5. accept 建立与客户端连接 SocketChannel 用来与客户端之间通信
            // 5.1 非阻塞 线程还会继续运行 如果没有连接建立 sc是null
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected... {}", sc);
                // 5.2 非阻塞模式
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 6. 接收客户端发送的数据
                // 6.1 非阻塞 线程仍然会继续运行 如果没有读到数据 read返回0
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    debugAll(buffer);
                    buffer.clear();
                    log.debug("after read... {}", channel);
                }
            }
        }

    }
}
