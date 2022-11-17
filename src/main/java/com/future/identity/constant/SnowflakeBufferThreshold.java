package com.future.identity.constant;

/**
 * Snowflake configuration
 *
 * @author liuyunfei
 */
public enum SnowflakeBufferThreshold {

    /**
     * pow min
     */
    MIN_POWER(0),

    /**
     * pow max
     */
    MAX_POWER(5),

    /**
     * pow default
     */
    DEFAULT_POWER(3),

    /**
     * fac min
     */
    MIN_PADDING_FACTOR(10),

    /**
     * fac max
     */
    MAX_PADDING_FACTOR(90),

    /**
     * fac default
     */
    DEFAULT_PADDING_FACTOR(50);


    public final int threshold;

    SnowflakeBufferThreshold(int threshold) {
        this.threshold = threshold;
    }

}
