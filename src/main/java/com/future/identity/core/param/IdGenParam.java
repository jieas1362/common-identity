package com.future.identity.core.param;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * params for IdentityGenerator
 *
 * @author liuyunfei
 */
public final class IdGenParam {

    private final int dataCenter;

    private final int worker;

    private final long lastSeconds;

    private final long bootSeconds;

    private final Consumer<Long> secondsRecorder;

    private final Long recordInterval;

    private final Consumer<Long> maximumTimeAlarm;

    private final Integer bufferPower;

    private final Integer paddingFactor;

    private final ExecutorService executorService;

    private final Boolean paddingScheduled;

    private final ScheduledExecutorService scheduledExecutorService;

    private final Long paddingScheduledInitialDelayMillis;

    private final Long paddingScheduledDelayMillis;

    public IdGenParam(int dataCenter, int worker, long lastSeconds, long bootSeconds, Consumer<Long> secondsRecorder,
                      Long recordInterval, Consumer<Long> maximumTimeAlarm, Integer bufferPower, Integer paddingFactor,
                      ExecutorService executorService, Boolean paddingScheduled, ScheduledExecutorService scheduledExecutorService,
                      Long paddingScheduledInitialDelayMillis, Long paddingScheduledDelayMillis) {
        this.dataCenter = dataCenter;
        this.worker = worker;
        this.lastSeconds = lastSeconds;
        this.bootSeconds = bootSeconds;
        this.secondsRecorder = secondsRecorder;
        this.recordInterval = recordInterval;
        this.maximumTimeAlarm = maximumTimeAlarm;
        this.bufferPower = bufferPower;
        this.paddingFactor = paddingFactor;
        this.executorService = executorService;
        this.paddingScheduled = paddingScheduled;
        this.scheduledExecutorService = scheduledExecutorService;
        this.paddingScheduledInitialDelayMillis = paddingScheduledInitialDelayMillis;
        this.paddingScheduledDelayMillis = paddingScheduledDelayMillis;
    }

    public int getDataCenter() {
        return dataCenter;
    }

    public int getWorker() {
        return worker;
    }

    public long getLastSeconds() {
        return lastSeconds;
    }

    public long getBootSeconds() {
        return bootSeconds;
    }

    public Consumer<Long> getSecondsRecorder() {
        return secondsRecorder;
    }

    public Long getRecordInterval() {
        return recordInterval;
    }

    public Consumer<Long> getMaximumTimeAlarm() {
        return maximumTimeAlarm;
    }

    public Integer getBufferPower() {
        return bufferPower;
    }

    public Integer getPaddingFactor() {
        return paddingFactor;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public Boolean getPaddingScheduled() {
        return paddingScheduled;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public Long getPaddingScheduledInitialDelayMillis() {
        return paddingScheduledInitialDelayMillis;
    }

    public Long getPaddingScheduledDelayMillis() {
        return paddingScheduledDelayMillis;
    }

    @Override
    public String toString() {
        return "IdGenParam{" +
                "dataCenter=" + dataCenter +
                ", worker=" + worker +
                ", lastSeconds=" + lastSeconds +
                ", bootSeconds=" + bootSeconds +
                ", secondsRecorder=" + secondsRecorder +
                ", recordInterval=" + recordInterval +
                ", maximumTimeAlarm=" + maximumTimeAlarm +
                ", bufferPower=" + bufferPower +
                ", paddingFactor=" + paddingFactor +
                ", executorService=" + executorService +
                ", paddingScheduled=" + paddingScheduled +
                ", scheduledExecutorService=" + scheduledExecutorService +
                ", paddingScheduledInitialDelayMillis=" + paddingScheduledInitialDelayMillis +
                ", paddingScheduledDelayMillis=" + paddingScheduledDelayMillis +
                '}';
    }

}
