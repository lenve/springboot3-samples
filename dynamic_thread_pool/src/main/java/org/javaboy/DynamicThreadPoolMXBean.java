package org.javaboy;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
import java.util.concurrent.ThreadPoolExecutor;

public interface DynamicThreadPoolMXBean {
    int getCorePoolSize();
    void setCorePoolSize(int corePoolSize);
    int getMaximumPoolSize();
    void setMaximumPoolSize(int maximumPoolSize);
    int getActiveCount();
    float getActiveRatio();
}