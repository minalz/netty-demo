package cn.minalz.netty.c4;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * await死锁检查
 * @author zhouwei
 * @date 2024/6/26 10:45
 */
@Slf4j
public class Demo6 {

    public static void main(String[] args) {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

        eventExecutors.submit(() -> {
            log.debug("1");
            try {
                // 不能捕获 InterruptedException异常
                // 死锁检查抛出的 BlockingOperationException 会继续向上传播
                // 提交的任务会被包装为 PromiseTask 它的run方法中会catch所有异常然后设置为 Promise 的失败结果而不会抛出
                promise.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("2");
        });

        eventExecutors.submit(() -> {
           log.debug("3");
            try {
                promise.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("4");
        });
    }
}
