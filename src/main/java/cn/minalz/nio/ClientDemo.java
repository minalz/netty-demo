package cn.minalz.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author zhouwei
 * @date 2024/6/11 14:59
 */
@Slf4j
public class ClientDemo {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        log.info("waiting...");
    }
}
