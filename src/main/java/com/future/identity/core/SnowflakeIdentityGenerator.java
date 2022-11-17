package com.future.identity.core;

import com.future.identity.core.exp.IdentityException;
import com.future.identity.core.param.SnowIdGenParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.future.base.constant.common.ProAttr.*;
import static com.future.base.util.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.future.base.util.base.ProChecker.isNull;
import static com.future.identity.constant.SnowflakeBits.*;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.ofInstant;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.ThreadLocalRandom.current;


/**
 * sync id generator by snowflake
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class SnowflakeIdentityGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeIdentityGenerator.class);

    /**
     * Legal length
     */
    private static final int BITS_64 = 64;
    /**
     * random advance time
     */
    private static final long RAN_ADVANCE_SEC_BOUND = 120L;

    /**
     * online seconds
     */
    private long bootSeconds;

    /**
     * max timeStamp,max sequence
     */
    private long maxStepTimestamp;
    private final long maxSequence;

    /**
     * current sequence,last timeStamp
     */
    private long sequence;
    private long stepSeconds;

    /**
     * The offset and mask used to generate the ID
     */
    private final int timeStampShift;
    private final long dataCenterWithWorkerBitsMask;

    /**
     * need to record last seconds?
     */
    private boolean notRecord;

    /**
     * last seconds recorder
     */
    private Consumer<Long> secondsRecorder;

    /**
     * last record seconds
     */
    private long lastRecordSeconds;

    /**
     * seconds record interval
     */
    private long recordInterval;

    /**
     * need to alarm?
     */
    private boolean notAlarm;

    /**
     * maximum time alarm
     */
    private Consumer<Long> maximumTimeAlarm;

    private ExecutorService executorService;

    public SnowflakeIdentityGenerator(SnowIdGenParam snowIdGenParam) {
        LOGGER.info("ProIdentityGenerator init, snowIdGenParam = {}", snowIdGenParam);

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
        long dataCenter = ofNullable(snowIdGenParam.getDataCenter()).filter(dc -> dc >= 0L).orElseThrow(() -> new IdentityException("dataCenter cannot be null or less than 0L"));
        long worker = ofNullable(snowIdGenParam.getWorker()).filter(w -> w >= 0L).orElseThrow(() -> new IdentityException("worker cannot be null or less than 0L"));

        if (dataCenter > maxDataCenter)
            throw new IdentityException("dataCenter cannot be greater than " + maxDataCenter);
        if (worker > maxWorker)
            throw new IdentityException("maxWorker cannot be greater than " + maxWorker);

        this.maxStepTimestamp = ~(-1L << timestampBits);

        long currentSecond = TIME_STAMP_GETTER.get();
        this.bootSeconds = ofNullable(snowIdGenParam.getBootSeconds()).filter(bs -> bs > 0L).orElse(ONLINE_TIME);
        long lastSeconds = ofNullable(snowIdGenParam.getLastSeconds()).filter(ls -> ls > 0).orElseGet(TIME_STAMP_GETTER);

        if (bootSeconds >= currentSecond || bootSeconds >= lastSeconds)
            throw new IdentityException("bootSeconds cannot be greater than " + currentSecond + " or " + lastSeconds);

        this.stepSeconds = lastSeconds - bootSeconds + 1L + current().nextLong(RAN_ADVANCE_SEC_BOUND);
        this.lastRecordSeconds = stepSeconds;

        if (stepSeconds > maxStepTimestamp)
            throw new IdentityException("stepSeconds cannot be greater than " + maxStepTimestamp);

        this.maxSequence = ~(-1L << sequenceBits);
        this.sequence = 0L;

        this.timeStampShift = dataCenterBits + workerBits + sequenceBits;
        this.dataCenterWithWorkerBitsMask = (dataCenter << (workerBits + sequenceBits)) | (worker << sequenceBits);

        this.executorService = snowIdGenParam.getExecutorService();
        if (isNull(this.executorService))
            throw new IdentityException("executorService can't be null");

        this.secondsRecorder = snowIdGenParam.getSecondsRecorder();
        this.notRecord = isNull(this.secondsRecorder);
        this.recordInterval = ofNullable(snowIdGenParam.getRecordInterval()).filter(ri -> ri >= 1L).orElse(1L);

        this.maximumTimeAlarm = snowIdGenParam.getMaximumTimeAlarm();
        this.notAlarm = isNull(this.maximumTimeAlarm);

        ZoneId zoneId = ZoneId.of(TIME_ZONE);
        LOGGER.info(
                "Initialized ProIdentityBuffer successfully, snowIdGenParam = {}, dataCenter = {}, worker = {}, " +
                        "maxStepTimestamp = {}, maxSequence = {}, sequence = {}, stepSeconds = {}, bootTime = {}, lastTime = {}, stepTime = {}",
                snowIdGenParam, dataCenter, worker, maxStepTimestamp, maxSequence, sequence, this.stepSeconds,
                ofInstant(ofEpochSecond(bootSeconds), zoneId).format(DATE_TIME_FORMATTER),
                ofInstant(ofEpochSecond(lastSeconds), zoneId).format(DATE_TIME_FORMATTER),
                ofInstant(ofEpochSecond(stepSeconds), zoneId).format(DATE_TIME_FORMATTER)
        );
    }

    /**
     * alarm for Maximum time to reach
     */
    private final Consumer<Long> MAXIMUM_TIME_ALARM = stepSeconds -> {
        if (notAlarm)
            return;

        if (stepSeconds + bootSeconds > maxStepTimestamp) {
            long maxTimeStamp = maxStepTimestamp + bootSeconds;
            LOGGER.error("Maximum time to reach {} , !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", maxTimeStamp);
            synchronized (this) {
                maximumTimeAlarm.accept(maxTimeStamp);
            }
        }
    };

    /**
     * record last seconds
     */
    @SuppressWarnings("DoubleCheckedLocking")
    private final Consumer<Long> STEP_SECONDS_RECORDER = stepSeconds -> {
        if (notRecord)
            return;

        if (stepSeconds - lastRecordSeconds > recordInterval)
            synchronized (this) {
                if (stepSeconds - lastRecordSeconds > recordInterval) {
                    secondsRecorder.accept(stepSeconds + bootSeconds);
                    lastRecordSeconds = stepSeconds;
                }
            }
    };

    /**
     * last seconds processor
     */
    private final Consumer<Long> LAST_SECONDS_PROCESSOR = stepSeconds ->
            executorService.execute(() -> {
                MAXIMUM_TIME_ALARM.accept(stepSeconds);
                STEP_SECONDS_RECORDER.accept(stepSeconds);
            });

    /**
     * generate a snowflake id
     *
     * @return
     */
    public long generate() {
        final long seq;
        final long timeStamp;
        boolean stepAdvanced = false;
        synchronized (this) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0L) {
                ++stepSeconds;
                stepAdvanced = true;
            }
            seq = sequence;
            timeStamp = stepSeconds;
        }

        if (stepAdvanced)
            LAST_SECONDS_PROCESSOR.accept(timeStamp);

        return (timeStamp << timeStampShift) | dataCenterWithWorkerBitsMask | seq;
    }

}
