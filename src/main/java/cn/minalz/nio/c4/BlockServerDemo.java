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
 * 阻塞模式服务端
 * @author zhouwei
 * @date 2024/6/11 14:51
 */
@Slf4j
public class BlockServerDemo {

    public static void main(String[] args) throws IOException {
        // 1.创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 2.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 3.绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        // 4.连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 5.accept 建立与客户端连接 socketChannel 用来与客户端之间通信
            log.debug("connecting...");
            SocketChannel sc = ssc.accept();
            log.debug("connected... {}", sc);
            channels.add(sc);
            for (SocketChannel channel : channels) {
                // 6.接收客户端发送的数据
                log.debug("before read... {}", channel);
                channel.read(buffer); // 阻塞方法 线程停止运行
                buffer.flip();
                debugAll(buffer);
                buffer.clear();
                log.debug("after read... {}", channel);
            }
        }
    }
}
