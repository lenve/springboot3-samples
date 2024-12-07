package org.javaboy;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

public class DynamicThreadPoolMBean extends StandardMBean implements DynamicThreadPoolMXBean {

    private DynamicThreadPool dynamicThreadPool;

    public DynamicThreadPoolMBean(DynamicThreadPool dynamicThreadPool) throws Exception {
        super(DynamicThreadPoolMXBean.class);
        this.dynamicThreadPool = dynamicThreadPool;
        registerMBean();
    }

    private void registerMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("org.javaboy:type=DynamicThreadPool");
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCorePoolSize() {
        return dynamicThreadPool.getThreadPoolExecutor().getCorePoolSize();
    }

    @Override
    public void setCorePoolSize(int corePoolSize) {
        dynamicThreadPool.setCorePoolSize(corePoolSize);
    }

    @Override
    public int getMaximumPoolSize() {
        return dynamicThreadPool.getThreadPoolExecutor().getMaximumPoolSize();
    }

    @Override
    public void setMaximumPoolSize(int maximumPoolSize) {
        dynamicThreadPool.setMaximumPoolSize(maximumPoolSize);
    }

    @Override
    public int getActiveCount() {
        return dynamicThreadPool.getThreadPoolExecutor().getActiveCount();
    }

    @Override
    public float getActiveRatio() {
        return getActiveCount() * 1.0f / dynamicThreadPool.getThreadPoolExecutor().getPoolSize();
    }
}