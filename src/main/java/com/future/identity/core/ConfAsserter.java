package com.future.identity.core;

import com.future.identity.api.conf.IdentityConf;
import com.future.identity.core.exp.IdentityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.future.base.util.base.ProChecker.isNull;
import static com.future.identity.constant.SnowflakeBits.*;
import static com.future.identity.constant.SnowflakeBufferThreshold.MAX_PADDING_FACTOR;
import static com.future.identity.constant.SnowflakeBufferThreshold.MIN_PADDING_FACTOR;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * asserter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaMethodTooLong"})
public final class ConfAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfAsserter.class);

    /**
     * Legal length
     */
    private static final int BITS_64 = 64;

    /**
     * random advance time
     */
    private static final long MAX_RAN_ADVANCE_SEC_BOUND = 120L;

    /**
     * asserter conf
     *
     * @param identityConf
     */
    public static void assertConf(IdentityConf identityConf) {
        LOGGER.info("void assertConf(IdentityConf identityConf), identityConf = {}", identityConf);

        if (isNull(identityConf))
            throw new IdentityException("identityConf can't be null");

        int timestampBits = TIME_STAMP.len;
        int dataCenterBits = DATA_CENTER.len;
        int workerBits = WORKER.len;
        int sequenceBits = SEQUENCE.len;

        if (timestampBits < 1 || dataCenterBits < 1 || workerBits < 1 || sequenceBits < 1)
            throw new IdentityException("timestampBits,dataCenterBits,workerBits,sequenceBits must be greater than 0");
        if (BITS_64 != (1 + timestampBits + dataCenterBits + workerBits + sequenceBits))
            throw new IdentityException("sum of (timestampBits,dataCenterBits,workerBits,sequenceBits) must be 63");

        long maxDataCenter = ~(-1L << dataCenterBits);
        long maxWorker = ~(-1L << workerBits);
        Integer dataCenter = identityConf.getDataCenter();
        Integer worker = identityConf.getWorker();

        if (isNull(dataCenter) || dataCenter.longValue() > maxDataCenter)
            throw new IdentityException("dataCenter cannot be null or greater than " + maxDataCenter);
        if (isNull(worker) || worker.longValue() > maxWorker)
            throw new IdentityException("maxWorker cannot be null or greater than " + maxWorker);

        String serviceName = identityConf.getServiceName();
        if (isNull(serviceName) || "".equals(serviceName))
            throw new IdentityException("serviceName cannot be null or ''");

        long maxStepTimestamp = ~(-1L << timestampBits);

        long currentSecond = MILLISECONDS.toSeconds(currentTimeMillis());
        Long bootSeconds = identityConf.getBootSeconds();
        long lastSeconds = ofNullable(identityConf.getLastSecondsGetter())
                .map(Supplier::get)
                .filter(ls -> ls > 0)
                .orElseGet(() -> now().getEpochSecond());

        if (isNull(bootSeconds) || bootSeconds >= currentSecond || bootSeconds >= lastSeconds)
            throw new IdentityException("bootSeconds cannot be greater than " + currentSecond + " or " + lastSeconds);

        long stepSeconds = lastSeconds - bootSeconds + MAX_RAN_ADVANCE_SEC_BOUND;
        if (stepSeconds > maxStepTimestamp)
            throw new IdentityException("stepSeconds cannot be greater than " + maxStepTimestamp);

        Consumer<Long> maximumTimeAlarm = identityConf.getMaximumTimeAlarm();
        if (isNull(maximumTimeAlarm))
            throw new IdentityException("maximumTimeAlarm can't be null");

        Integer paddingFactor = identityConf.getPaddingFactor();
        if (isNull(paddingFactor) || paddingFactor < MIN_PADDING_FACTOR.threshold || paddingFactor > MAX_PADDING_FACTOR.threshold)
            throw new IdentityException("paddingFactor range must be in (" + MIN_PADDING_FACTOR.threshold + " - " + MAX_PADDING_FACTOR.threshold + ")");

        Integer paddingCorePoolSize = identityConf.getPaddingCorePoolSize();
        if (isNull(paddingCorePoolSize) || paddingCorePoolSize < 1)
            throw new IdentityException("paddingCorePoolSize can't be null or less than 1");

        Integer paddingMaximumPoolSize = identityConf.getPaddingMaximumPoolSize();
        if (isNull(paddingMaximumPoolSize) || paddingMaximumPoolSize < 1)
            throw new IdentityException("paddingMaximumPoolSize can't be null or less than 1");

        if (paddingMaximumPoolSize < paddingCorePoolSize)
            throw new IdentityException("paddingMaximumPoolSize can't less than paddingCorePoolSize");

        Long keepAliveSeconds = identityConf.getKeepAliveSeconds();
        if (isNull(keepAliveSeconds) || keepAliveSeconds < 1L)
            throw new IdentityException("keepAliveSeconds can't be null or less than 1");

        Integer paddingBlockingQueueSize = identityConf.getPaddingBlockingQueueSize();
        if (isNull(paddingBlockingQueueSize) || paddingBlockingQueueSize < 1)
            throw new IdentityException("paddingBlockingQueueSize can't be null or less than 1");

        if (ofNullable(identityConf.getPaddingScheduled()).orElse(false)) {
            Integer paddingScheduledCorePoolSize = identityConf.getPaddingScheduledCorePoolSize();
            if (isNull(paddingScheduledCorePoolSize) || paddingScheduledCorePoolSize < 1)
                throw new IdentityException("paddingScheduledCorePoolSize can't be null or less than 1");

            Long paddingScheduledInitialDelayMillis = identityConf.getPaddingScheduledInitialDelayMillis();
            if (isNull(paddingScheduledInitialDelayMillis) || paddingScheduledInitialDelayMillis < 1)
                throw new IdentityException("paddingScheduledInitialDelayMillis can't be null or less than 1");

            Long paddingScheduledDelayMillis = identityConf.getPaddingScheduledDelayMillis();
            if (isNull(paddingScheduledDelayMillis) || paddingScheduledDelayMillis < 1)
                throw new IdentityException("paddingScheduledDelayMillis can't be null or less than 1");
        }
    }

}
