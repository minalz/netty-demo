package cn.minalz.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

@Slf4j
public class ChannelDemo1 {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("data.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            do {
                // 向buffer写入
                int len = channel.read(buffer);
                log.debug("读到字节数：{}", len);
                if (len == -1) {
                    break;
                }
                // 切换buffer读模式
                buffer.flip();
                debugAll(buffer);
                while (buffer.hasRemaining()) {
                    log.debug("{}", (char) buffer.get());
                }
                // 切换buffer写模式
                buffer.clear();
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
