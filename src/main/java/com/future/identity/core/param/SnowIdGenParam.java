package com.future.identity.core.param;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * params for SnowflakeIdentityGenerator
 *
 * @author liuyunfei
 */
public final class SnowIdGenParam {

    private final Integer dataCenter;

    private final Integer worker;

    private final Long lastSeconds;

    private final Long bootSeconds;

    private final Consumer<Long> secondsRecorder;

    private final Long recordInterval;

    private final Consumer<Long> maximumTimeAlarm;

    private final ExecutorService executorService;

    public SnowIdGenParam(Integer dataCenter, Integer worker, Long lastSeconds, Long bootSeconds,
                          Consumer<Long> secondsRecorder, Long recordInterval, Consumer<Long> maximumTimeAlarm,
                          ExecutorService executorService) {
        this.dataCenter = dataCenter;
        this.worker = worker;
        this.lastSeconds = lastSeconds;
        this.bootSeconds = bootSeconds;
        this.secondsRecorder = secondsRecorder;
        this.recordInterval = recordInterval;
        this.maximumTimeAlarm = maximumTimeAlarm;
        this.executorService = executorService;
    }

    public Integer getDataCenter() {
        return dataCenter;
    }

    public Integer getWorker() {
        return worker;
    }

    public Long getLastSeconds() {
        return lastSeconds;
    }

    public Long getBootSeconds() {
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

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public String toString() {
        return "SnowIdGenParam{" +
                "dataCenter=" + dataCenter +
                ", worker=" + worker +
                ", lastSeconds=" + lastSeconds +
                ", bootSeconds=" + bootSeconds +
                ", secondsRecorder=" + secondsRecorder +
                ", recordInterval=" + recordInterval +
                ", maximumTimeAlarm=" + maximumTimeAlarm +
                ", executorService=" + executorService +
                '}';
    }

}
