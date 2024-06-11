package cn.minalz.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author zhouwei
 * @date 2024/6/11 11:02
 */
@Slf4j
public class WalkDeleteFile {

    /**
     * 删除是危险操作，确保要递归删除的文件夹没有重要内容
     */
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:\\jdk17 - 副本");
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }
}
