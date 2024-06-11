package cn.minalz.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 遍历目录和问价
 * @author zhouwei
 * @date 2024/6/11 10:53
 */
@Slf4j
public class WalkDirectoryAndFiles {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\jdk17");
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                log.info("dir: {}", dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info("file: {}", file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        log.info("dirCount: {}, fileCount: {}", dirCount, fileCount);
    }
}
