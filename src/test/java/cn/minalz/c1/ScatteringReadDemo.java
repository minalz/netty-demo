package cn.minalz.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * @author zhouwei
 * @date 2024/6/11 10:00
 */
public class ScatteringReadDemo {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("3parts.txt", "rw");) {
            FileChannel channel = file.getChannel();
            ByteBuffer a = ByteBuffer.allocate(3);
            ByteBuffer b = ByteBuffer.allocate(3);
            ByteBuffer c = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{a, b, c});
            a.flip();
            b.flip();
            c.flip();
            debugAll(a);
            debugAll(b);
            debugAll(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
