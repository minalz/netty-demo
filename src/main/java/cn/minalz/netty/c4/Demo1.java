package cn.minalz.netty.c4;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * 异步处理任务1
 * @author zhouwei
 * @date 2024/6/26 10:15
 */
@Slf4j
public class Demo1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);
        eventExecutors.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("set success, {}", 10);
            promise.setSuccess(10);
        });

        log.debug("start...");
        // 还拿不到结果 因为异步
        log.debug("{}", promise.getNow());
        // get这里是阻塞
        log.debug("{}", promise.get());
    }
}
