package cn.minalz.nio.c3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 拷贝多级目录
 * @author zhouwei
 * @date 2024/6/11 11:04
 */
@Slf4j
public class WalkCopyFile {

    public static void main(String[] args) throws IOException {
        StopWatch stopWatch = new StopWatch();
        String source = "D:\\companyfile";
        String target = "D:\\companyfileaaa";
        stopWatch.start("copy start");
        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                // 是目录
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                }
                // 是普通文件
                else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        stopWatch.stop();
        log.info("stopWatch times: {}", stopWatch.prettyPrint());
    }
}
