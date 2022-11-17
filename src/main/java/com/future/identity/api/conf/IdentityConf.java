package com.future.identity.api.conf;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * identity conf
 *
 * @author liuyunfei
 */

@SuppressWarnings("JavaDoc")
public interface IdentityConf {

    /**
     * datacenter no
     *
     * @return
     */
    Integer getDataCenter();

    /**
     * worker no
     *
     * @return
     */
    Integer getWorker();

    /**
     * service name
     *
     * @return
     */
    String getServiceName();

    /**
     * online seconds
     *
     * @return
     */
    Long getBootSeconds();

    /**
     * last id generated seconds
     *
     * @return
     */
    Supplier<Long> getLastSecondsGetter();

    /**
     * last id generated seconds recorder
     *
     * @return
     */
    Consumer<Long> getSecondsRecorder();

    /**
     * time stamp record interval
     *
     * @return
     */
    Long getRecordInterval();

    /**
     * alarm for timestamp reach max
     *
     * @return
     */
    Consumer<Long> getMaximumTimeAlarm();

    /**
     * buffer size (power of 2)
     *
     * @return
     */
    Integer getBufferPower();

    /**
     * threshold for padding (percentage)
     *
     * @return
     */
    Integer getPaddingFactor();

    /**
     * async padding executor core pool size
     *
     * @return
     */
    Integer getPaddingCorePoolSize();

    /**
     * async padding executor max pool size
     *
     * @return
     */
    Integer getPaddingMaximumPoolSize();

    /**
     * async padding executor max pool keep alive seconds
     *
     * @return
     */
    Long getKeepAliveSeconds();

    /**
     * async padding executor max pool blocking queue size
     *
     * @return
     */
    Integer getPaddingBlockingQueueSize();

    /**
     * enable scheduled padding
     *
     * @return
     */
    Boolean getPaddingScheduled();

    /**
     * scheduled padding executor core pool size
     *
     * @return
     */
    Integer getPaddingScheduledCorePoolSize();

    /**
     * scheduled padding init delay millis
     *
     * @return
     */
    Long getPaddingScheduledInitialDelayMillis();

    /**
     * scheduled padding interval delay millis
     *
     * @return
     */
    Long getPaddingScheduledDelayMillis();

}
