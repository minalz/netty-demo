package cn.minalz.netty.c4;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * 同步处理任务失败2
 * @author zhouwei
 * @date 2024/6/26 10:34
 */
@Slf4j
public class Demo4 {

    public static void main(String[] args) throws InterruptedException {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        log.debug("start...");
        log.debug("{}", promise.getNow());
        promise.await();
        log.debug("result {}", (promise.isSuccess() ? promise.getNow() : promise.cause()).toString());
    }
}
