package cn.minalz.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

@Slf4j
public class StringConvertByteBufferDemo {

    public static void main(String[] args) {
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("你好");
        ByteBuffer buffer2 = Charset.forName("utf-8").encode("你好");

        debugAll(buffer1);
        debugAll(buffer2);

        CharBuffer buffer3 = StandardCharsets.UTF_8.decode(buffer1);
        log.info("{}", buffer3.getClass());
        log.info("{}", buffer3);
    }
}
