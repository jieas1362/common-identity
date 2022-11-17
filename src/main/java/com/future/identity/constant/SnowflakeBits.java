package com.future.identity.constant;

/**
 * Snowflake configuration
 *
 * @author liuyunfei
 */
public enum SnowflakeBits {

    /**
     * Second-level timestamp, length 33, maximum support 272 years, based on 2021/4/19 12:00 as the baseline, it can be used for 221 years
     */
    TIME_STAMP(33),

    /**
     * Data center ID, length 6, supports up to 64 data centers
     */
    DATA_CENTER(6),

    /**
     * Machine ID, length 9, each data center supports up to 512 machines
     */
    WORKER(9),

    /**
     * Self-increasing sequence, length 15, single machine single second 32768, can apply to the future time
     */
    SEQUENCE(15);

    /**
     * len
     */
    public final int len;

    SnowflakeBits(int len) {
        this.len = len;
    }
}
