package cn.minalz.c1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zhouwei
 * @date 2024/6/11 10:26
 */
@Slf4j
public class ChannelFromToDemo {

    /**
     * 大于2g大小的文件传输（小文件传输当然也没问题）
     */
    public static void main(String[] args) {
        String FROM = "data.txt";
        String TO = "to.txt";
        long start = System.nanoTime();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel()) {
            long size = from.size();
            for (long left = size; left > 0;) {
                log.info("position: {}, left: {}", (size - left), left);
                // transferTo返回的是实际传输的字节数
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        log.info("transferTo 用时：{}", (end - start) / 1000_000.0);
    }

    /**
     * 小文件传输 < 2g
     */
    /*public static void main(String[] args) {
        String FROM = "data.txt";
        String TO = "to.txt";
        long start = System.nanoTime();
        try (FileChannel from = new FileInputStream(FROM).getChannel();
             FileChannel to = new FileOutputStream(TO).getChannel()) {
            from.transferTo(0, from.size(), to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.nanoTime();
        log.info("transferTo 用时：{}", (end - start) / 1000_000.0);
    }*/
}
