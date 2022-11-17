package com.future.identity.component;

import com.future.base.constant.common.Symbol;
import com.future.identity.api.conf.IdentityConf;
import com.future.identity.core.ProIdentityGenerator;
import com.future.identity.core.SnowflakeIdentityParser;
import com.future.identity.core.exp.IdentityException;
import com.future.identity.core.param.IdGenParam;
import com.future.identity.model.IdentityElement;
import net.openhft.affinity.AffinityThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.future.base.util.base.ProChecker.isNotNull;
import static com.future.base.util.base.ProChecker.isNull;
import static com.future.identity.core.ConfAsserter.assertConf;
import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * IdentityProcessor is the Bean of the context.It also provides a simple static method for parse ID.
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class ProIdentityProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProIdentityProcessor.class);

    /**
     * key parts
     */
    private static final String GEN_KEY_PRE = "GEN:";
    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    /**
     * key pre
     */
    private String handlerKeyPre;

    /**
     * param
     */
    private IdGenParam idGenParam;

    /**
     * generators holder
     */
    private final Map<String, ProIdentityGenerator> GENERATORS = new ConcurrentHashMap<>();

    private static final String THREAD_NAME = "IdentityProcessor-thread";
    private static final int RANDOM_LEN = 4;

    /**
     * constructor
     *
     * @param identityConf
     */
    public ProIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("ProIdentityProcessor(IdentityConf identityConf), identityConf = {}", identityConf);

        assertConf(identityConf);

        String serviceName = identityConf.getServiceName() + PAR_CONCATENATION + identityConf.getDataCenter() + PAR_CONCATENATION + identityConf.getWorker();
        handlerKeyPre = (GEN_KEY_PRE + serviceName + PAR_CONCATENATION).intern();

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME + randomAlphabetic(RANDOM_LEN), SAME_CORE);

        Boolean paddingScheduled = identityConf.getPaddingScheduled();
        idGenParam = new IdGenParam(identityConf.getDataCenter(), identityConf.getWorker(), ofNullable(identityConf.getLastSecondsGetter()).map(Supplier::get).filter(ls -> ls > 0)
                .orElseGet(() -> now().getEpochSecond()), identityConf.getBootSeconds(), identityConf.getSecondsRecorder(), identityConf.getRecordInterval(), identityConf.getMaximumTimeAlarm(), identityConf.getBufferPower(),
                identityConf.getPaddingFactor(), new ThreadPoolExecutor(identityConf.getPaddingCorePoolSize(), identityConf.getPaddingMaximumPoolSize(),
                identityConf.getKeepAliveSeconds(), SECONDS, new ArrayBlockingQueue<>(identityConf.getPaddingBlockingQueueSize()),
                threadFactory, (r, executor) -> {
            LOGGER.error("Asynchronous padding thread rejected");
            r.run();
        }), paddingScheduled,
                ofNullable(paddingScheduled).orElse(false) ?
                        new ScheduledThreadPoolExecutor(identityConf.getPaddingScheduledCorePoolSize(), threadFactory, (r, executor) -> {
                            LOGGER.error("Timed padding thread rejected");
                            r.run();
                        })
                        :
                        null
                , identityConf.getPaddingScheduledInitialDelayMillis(), identityConf.getPaddingScheduledDelayMillis());

        LOGGER.info("IdentityProcessor init success, idGenParam = {}", idGenParam);
    }

    /**
     * key gen
     */
    private final Function<Class<?>, String> KEY_GEN = entityClz -> {
        if (isNotNull(entityClz))
            return (handlerKeyPre + entityClz.getName().intern()).intern();

        throw new IdentityException("entityClz can't be null");
    };

    /**
     * handler getter
     */
    private final Function<String, ProIdentityGenerator> HANDLER_GETTER = key -> {
        ProIdentityGenerator generator = GENERATORS.get(key.intern());
        if (isNotNull(generator))
            return generator;

        synchronized (key.intern()) {
            if (isNull((generator = GENERATORS.get(key)))) {
                generator = new ProIdentityGenerator(idGenParam);
                GENERATORS.put(key, generator);
                LOGGER.info("processor init, key = {}", key);
            }
        }

        return generator;
    };

    /**
     * Generate snowflake ID based on service name and table name
     *
     * @param entityClz
     * @return
     */
    public long generate(Class<?> entityClz) {
        return HANDLER_GETTER.apply(KEY_GEN.apply(entityClz).intern()).generate();
    }

    /**
     * Parse snowflake ID attributes
     *
     * @param id
     * @return
     */
    public IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
