package cn.minalz.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhouwei
 * @date 2024/6/11 10:58
 */
@Slf4j
public class WalkJarFile {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\jdk17");
        AtomicInteger jarCount = new AtomicInteger();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toFile().getName().endsWith(".jar")) {
                    jarCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        log.info("jarCount: {}", jarCount);
    }
}
