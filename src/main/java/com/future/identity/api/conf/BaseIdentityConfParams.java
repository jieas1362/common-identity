package com.future.identity.api.conf;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * identity params
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public abstract class BaseIdentityConfParams implements IdentityConf {

    protected Integer dataCenter;

    protected Integer worker;

    protected String serviceName;

    protected Long bootSeconds;

    protected Integer bufferPower;

    protected Integer paddingFactor;

    protected Integer paddingCorePoolSize;

    protected Integer paddingMaximumPoolSize;

    protected Long keepAliveSeconds;

    protected Integer paddingBlockingQueueSize;

    protected Boolean paddingScheduled;

    protected Integer paddingScheduledCorePoolSize;

    protected Long paddingScheduledInitialDelayMillis;

    protected Long paddingScheduledDelayMillis;

    public BaseIdentityConfParams() {
    }

    public BaseIdentityConfParams(Integer dataCenter, Integer worker, String serviceName,
                                  Long bootSeconds, Integer bufferPower, Integer paddingFactor,
                                  Integer paddingCorePoolSize, Integer paddingMaximumPoolSize,
                                  Long keepAliveSeconds, Integer paddingBlockingQueueSize,
                                  Boolean paddingScheduled, Integer paddingScheduledCorePoolSize,
                                  Long paddingScheduledInitialDelayMillis, Long paddingScheduledDelayMillis) {
        this.dataCenter = dataCenter;
        this.worker = worker;
        this.serviceName = serviceName;
        this.bootSeconds = bootSeconds;
        this.bufferPower = bufferPower;
        this.paddingFactor = paddingFactor;
        this.paddingCorePoolSize = paddingCorePoolSize;
        this.paddingMaximumPoolSize = paddingMaximumPoolSize;
        this.keepAliveSeconds = keepAliveSeconds;
        this.paddingBlockingQueueSize = paddingBlockingQueueSize;
        this.paddingScheduled = paddingScheduled;
        this.paddingScheduledCorePoolSize = paddingScheduledCorePoolSize;
        this.paddingScheduledInitialDelayMillis = paddingScheduledInitialDelayMillis;
        this.paddingScheduledDelayMillis = paddingScheduledDelayMillis;
    }

    @Override
    public Integer getDataCenter() {
        return dataCenter;
    }

    @Override
    public Integer getWorker() {
        return worker;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Long getBootSeconds() {
        return bootSeconds;
    }

    @Override
    public abstract Supplier<Long> getLastSecondsGetter();

    @Override
    public abstract Consumer<Long> getSecondsRecorder();

    @Override
    public abstract Consumer<Long> getMaximumTimeAlarm();

    @Override
    public Integer getBufferPower() {
        return bufferPower;
    }

    @Override
    public Integer getPaddingFactor() {
        return paddingFactor;
    }

    @Override
    public Integer getPaddingCorePoolSize() {
        return paddingCorePoolSize;
    }

    @Override
    public Integer getPaddingMaximumPoolSize() {
        return paddingMaximumPoolSize;
    }

    @Override
    public Long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    @Override
    public Integer getPaddingBlockingQueueSize() {
        return paddingBlockingQueueSize;
    }

    @Override
    public Boolean getPaddingScheduled() {
        return paddingScheduled;
    }

    @Override
    public Integer getPaddingScheduledCorePoolSize() {
        return paddingScheduledCorePoolSize;
    }

    @Override
    public Long getPaddingScheduledInitialDelayMillis() {
        return paddingScheduledInitialDelayMillis;
    }

    @Override
    public Long getPaddingScheduledDelayMillis() {
        return paddingScheduledDelayMillis;
    }

    public void setDataCenter(Integer dataCenter) {
        this.dataCenter = dataCenter;
    }

    public void setBootSeconds(Long bootSeconds) {
        this.bootSeconds = bootSeconds;
    }

    public void setWorker(Integer worker) {
        this.worker = worker;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setBufferPower(Integer bufferPower) {
        this.bufferPower = bufferPower;
    }

    public void setPaddingFactor(Integer paddingFactor) {
        this.paddingFactor = paddingFactor;
    }

    public void setPaddingCorePoolSize(Integer paddingCorePoolSize) {
        this.paddingCorePoolSize = paddingCorePoolSize;
    }

    public void setPaddingMaximumPoolSize(Integer paddingMaximumPoolSize) {
        this.paddingMaximumPoolSize = paddingMaximumPoolSize;
    }

    public void setKeepAliveSeconds(Long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setPaddingBlockingQueueSize(Integer paddingBlockingQueueSize) {
        this.paddingBlockingQueueSize = paddingBlockingQueueSize;
    }

    public void setPaddingScheduled(Boolean paddingScheduled) {
        this.paddingScheduled = paddingScheduled;
    }

    public void setPaddingScheduledCorePoolSize(Integer paddingScheduledCorePoolSize) {
        this.paddingScheduledCorePoolSize = paddingScheduledCorePoolSize;
    }

    public void setPaddingScheduledInitialDelayMillis(Long paddingScheduledInitialDelayMillis) {
        this.paddingScheduledInitialDelayMillis = paddingScheduledInitialDelayMillis;
    }

    public void setPaddingScheduledDelayMillis(Long paddingScheduledDelayMillis) {
        this.paddingScheduledDelayMillis = paddingScheduledDelayMillis;
    }

}
