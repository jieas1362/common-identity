package com.future.identity.core;

import com.future.identity.core.param.IdBufferParam;
import com.future.identity.core.param.IdGenParam;
import com.future.identity.core.param.SnowIdGenParam;
import com.future.identity.model.IdentityElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

import static com.future.identity.constant.SnowflakeBits.SEQUENCE;
import static com.future.identity.constant.SnowflakeBufferThreshold.*;
import static java.util.Optional.ofNullable;

/**
 * buffered generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class ProIdentityGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProIdentityGenerator.class);

    /**
     * generator
     */
    private final SnowflakeIdentityGenerator snowflakeIdentityGenerator;

    /**
     * buffer
     */
    private final SnowflakeIdentityBuffer snowflakeIdentityBuffer;

    /**
     * constructor
     *
     * @param idGenParam
     */
    public ProIdentityGenerator(IdGenParam idGenParam) {
        LOGGER.info("ProIdentityGenerator init, idGenParam = {}", idGenParam);

        int sequenceBits = SEQUENCE.len;

        ExecutorService executorService = idGenParam.getExecutorService();

        this.snowflakeIdentityGenerator = new SnowflakeIdentityGenerator(new SnowIdGenParam(idGenParam.getDataCenter(), idGenParam.getWorker(), idGenParam.getLastSeconds(),
                idGenParam.getBootSeconds(), idGenParam.getSecondsRecorder(), idGenParam.getRecordInterval(), idGenParam.getMaximumTimeAlarm(), executorService));

        int bufferSize = ((int) ~(-1L << sequenceBits) + 1) <<
                ofNullable(idGenParam.getBufferPower()).filter(p -> p >= MIN_POWER.threshold && p <= MAX_POWER.threshold).orElse(DEFAULT_POWER.threshold);
        this.snowflakeIdentityBuffer = new SnowflakeIdentityBuffer(new IdBufferParam(snowflakeIdentityGenerator, executorService, idGenParam.getPaddingScheduled(),
                idGenParam.getScheduledExecutorService(), idGenParam.getPaddingScheduledInitialDelayMillis(), idGenParam.getPaddingScheduledDelayMillis(), bufferSize,
                ofNullable(idGenParam.getPaddingFactor()).filter(f -> f >= MIN_PADDING_FACTOR.threshold && f <= MAX_PADDING_FACTOR.threshold).orElse(DEFAULT_PADDING_FACTOR.threshold)));

        LOGGER.info("Initialized ProBufferedIdentityGenerator successfully, idGenParam = {}", idGenParam);
    }

    /**
     * Get id
     *
     * @return
     */
    public long generate() {
        try {
            return snowflakeIdentityBuffer.take();
        } catch (Exception e) {
            LOGGER.error("take id from buffer fail, e =  ", e);
            return snowflakeIdentityGenerator.generate();
        }
    }

    /**
     * Parse id
     *
     * @param id
     * @return
     */
    public IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
