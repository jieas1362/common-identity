package com.future.identity.core;

import com.future.identity.model.IdentityElement;

import static com.future.identity.constant.SnowflakeBits.*;

/**
 * static id parser
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class SnowflakeIdentityParser {

    /**
     * Legal length
     */
    private static final int BITS_64 = 64;

    /**
     * The offset used to parse ID attributes
     */
    private static final int SEQUENCE_LEFT_SHIFT = BITS_64 - SEQUENCE.len;
    private static final int WORKER_LEFT_SHIFT = 1 + TIME_STAMP.len + DATA_CENTER.len;
    private static final int WORKER_RIGHT_SHIFT = BITS_64 - WORKER.len;
    private static final int DATA_CENTER_LEFT_SHIFT = 1 + TIME_STAMP.len;
    private static final int DATA_CENTER_RIGHT_SHIFT = BITS_64 - DATA_CENTER.len;
    private static final int SECONDS_RIGHT_SHIFT = DATA_CENTER.len + WORKER.len + SEQUENCE.len;

    /**
     * Parse id attribute
     *
     * @param id
     * @return
     */
    public static IdentityElement parse(long id) {
        return new IdentityElement(
                id >>> SECONDS_RIGHT_SHIFT,
                (id << DATA_CENTER_LEFT_SHIFT) >>> DATA_CENTER_RIGHT_SHIFT,
                (id << WORKER_LEFT_SHIFT) >>> WORKER_RIGHT_SHIFT,
                (id << SEQUENCE_LEFT_SHIFT) >>> SEQUENCE_LEFT_SHIFT
        );
    }

}
