package cn.minalz.nio.c1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static cn.minalz.utils.ByteBufferUtil.debugAll;

/**
 * @author zhouwei
 * @date 2024/6/11 10:10
 */
@Slf4j
public class TestDemo1 {

    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, World\nI'm ZhangSan\nHo".getBytes());
        split(source);
        source.put("w are you?\nHaHa!\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        int oldLimit = source.limit();
        for (int i = 0; i < oldLimit; i++) {
            if (source.get(i) == '\n') {
                log.info("i: {}", i);
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                source.limit(i + 1);
                target.put(source);
                debugAll(target);
                source.limit(oldLimit);
            }
        }
        source.compact();
    }
}
