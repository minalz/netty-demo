package cn.minalz.netty.c4;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步处理任务2
 * @author zhouwei
 * @date 2024/6/26 10:19
 */
@Slf4j
public class Demo2 {

    public static void main(String[] args) {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        // 设置回调，异步接收结果
        promise.addListener(future -> {
            // 这里的future就是上面的promise
            log.debug("{}", future.getNow());
        });

        // 等待1000 后设置成功结果
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
    }
}
