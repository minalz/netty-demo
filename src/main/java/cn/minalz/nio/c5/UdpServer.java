package cn.minalz.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * UDP是无连接的 client发送数据不会管server是否开启
 * server这边的receive方法会将接收到的数据存入ByteBuffer 但如果数据报文超过buffer大小 多出来的数据会被默默抛弃
 * @author zhouwei
 * @date 2024/6/14 15:32
 */
@Slf4j
public class UdpServer {

    public static void main(String[] args) {
        try (DatagramChannel dc = DatagramChannel.open()) {
            dc.socket().bind(new InetSocketAddress(9999));
            log.info("waiting...");
            ByteBuffer buffer = ByteBuffer.allocate(16);
            dc.receive(buffer);
            buffer.flip();
            debugAll(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
