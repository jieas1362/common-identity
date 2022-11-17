package com.future.identity.ioc;

import com.future.identity.api.conf.IdentityConf;
import com.future.identity.component.ProIdentityProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * conf snowflake bean
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {IdentityConf.class})
@AutoConfiguration
public class ProIdentityConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProIdentityConfiguration.class);

    @Bean
    ProIdentityProcessor proIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("ProIdentityProcessor proIdentityProcessor(IdentityConf identityConf), identityConf = {}", identityConf);
        return new ProIdentityProcessor(identityConf);
    }

}
