package com.future.identity.model;

import java.io.Serializable;

/**
 * snowflake id attrs
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class IdentityElement implements Serializable {

    private static final long serialVersionUID = 2576204414603993595L;

    /**
     * timestamp offset
     */
    private final long deltaSeconds;

    /**
     * data center serial number
     */
    private final long dataCenter;

    /**
     * working machine serial number
     */
    private final long worker;

    /**
     * sequence
     */
    private final long sequence;

    public IdentityElement(long deltaSeconds, long dataCenter, long worker, long sequence) {
        this.deltaSeconds = deltaSeconds;
        this.dataCenter = dataCenter;
        this.worker = worker;
        this.sequence = sequence;
    }

    public long getDeltaSeconds() {
        return deltaSeconds;
    }

    public long getDataCenter() {
        return dataCenter;
    }

    public long getWorker() {
        return worker;
    }

    public long getSequence() {
        return sequence;
    }

    @Override
    public String toString() {
        return "IdentityElement{" +
                "deltaSeconds=" + deltaSeconds +
                ", dataCenter=" + dataCenter +
                ", worker=" + worker +
                ", sequence=" + sequence +
                '}';
    }

}
