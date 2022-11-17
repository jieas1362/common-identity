package com.future.identity.core;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Padding bits are used to prevent false sharing
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc"})
final class IdentityAtomicLong extends AtomicLong {

    private static final long serialVersionUID = -3415778863941386253L;

    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public IdentityAtomicLong() {
        super();
    }

    public IdentityAtomicLong(long initialValue) {
        super(initialValue);
    }

    /**
     * prevent GC recycling
     *
     * @return
     */
    public long doNotRecycling() {
        return p1 & p2 & p3 & p4 & p5 & p6;
    }

}