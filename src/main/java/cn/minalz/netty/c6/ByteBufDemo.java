package cn.minalz.netty.c6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static cn.minalz.utils.LogUtil.log;

/**
 * 池化创建内存方法（直接内存 堆内存）
 * @author zhouwei
 * @date 2024/6/26 13:26
 */
public class ByteBufDemo {

    public static void main(String[] args) {
        // 默认容量是256
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        log(buffer);

        // 堆内存
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer();

        // 直接内存（默认创建就是直接内存）
        ByteBuf byteBuf1 = ByteBufAllocator.DEFAULT.directBuffer();
    }
}
