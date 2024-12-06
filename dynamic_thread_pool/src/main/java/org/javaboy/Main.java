package org.javaboy;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        DynamicThreadPool dynamicThreadPool = new DynamicThreadPool(2, 4, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10));
        DynamicThreadPoolMBean mBean = new DynamicThreadPoolMBean(dynamicThreadPool);

        while (true) {
            System.out.println("CorePoolSize:" + dynamicThreadPool.getThreadPoolExecutor().getCorePoolSize());
            System.out.println("MaximumPoolSize:" + dynamicThreadPool.getThreadPoolExecutor().getMaximumPoolSize());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}