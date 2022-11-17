package com.future.identity.core.param;

import com.future.identity.core.SnowflakeIdentityGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * params for IdentityBuffer
 *
 * @author liuyunfei
 */
public final class IdBufferParam {

    private final SnowflakeIdentityGenerator snowflakeIdentityGenerator;

    private final ExecutorService bufferPadExecutor;

    private final Boolean paddingScheduled;

    private final ScheduledExecutorService bufferPadSchedule;

    private final Long paddingScheduledInitialDelayMillis;

    private final Long paddingScheduledDelayMillis;

    private final int bufferSize;

    private final int paddingFactor;

    public IdBufferParam(SnowflakeIdentityGenerator snowflakeIdentityGenerator, ExecutorService bufferPadExecutor, Boolean paddingScheduled, ScheduledExecutorService bufferPadSchedule,
                         Long paddingScheduledInitialDelayMillis, Long paddingScheduledDelayMillis, int bufferSize, int paddingFactor) {
        this.snowflakeIdentityGenerator = snowflakeIdentityGenerator;
        this.bufferPadExecutor = bufferPadExecutor;
        this.paddingScheduled = paddingScheduled;
        this.bufferPadSchedule = bufferPadSchedule;
        this.paddingScheduledInitialDelayMillis = paddingScheduledInitialDelayMillis;
        this.paddingScheduledDelayMillis = paddingScheduledDelayMillis;
        this.bufferSize = bufferSize;
        this.paddingFactor = paddingFactor;
    }

    public SnowflakeIdentityGenerator getSnowflakeIdentityGenerator() {
        return snowflakeIdentityGenerator;
    }

    public ExecutorService getBufferPadExecutor() {
        return bufferPadExecutor;
    }

    public Boolean getPaddingScheduled() {
        return paddingScheduled;
    }

    public ScheduledExecutorService getBufferPadSchedule() {
        return bufferPadSchedule;
    }

    public Long getPaddingScheduledInitialDelayMillis() {
        return paddingScheduledInitialDelayMillis;
    }

    public Long getPaddingScheduledDelayMillis() {
        return paddingScheduledDelayMillis;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public int getPaddingFactor() {
        return paddingFactor;
    }

    @Override
    public String toString() {
        return "IdBufferParam{" +
                "snowflakeIdentityGenerator=" + snowflakeIdentityGenerator +
                ", bufferPadExecutor=" + bufferPadExecutor +
                ", paddingScheduled=" + paddingScheduled +
                ", bufferPadSchedule=" + bufferPadSchedule +
                ", paddingScheduledInitialDelayMillis=" + paddingScheduledInitialDelayMillis +
                ", paddingScheduledDelayMillis=" + paddingScheduledDelayMillis +
                ", bufferSize=" + bufferSize +
                ", paddingFactor=" + paddingFactor +
                '}';
    }

}
