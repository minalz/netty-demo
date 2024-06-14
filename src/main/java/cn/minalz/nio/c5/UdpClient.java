package cn.minalz.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * UDP客户端
 * @author zhouwei
 * @date 2024/6/14 15:36
 */
@Slf4j
public class UdpClient {

    public static void main(String[] args) {
        try (DatagramChannel dc = DatagramChannel.open()) {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello1234567890abcdef");
            dc.send(buffer, new InetSocketAddress("localhost", 9999));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
