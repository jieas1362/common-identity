package com.future.identity.api.common;

import com.future.identity.core.SnowflakeIdentityParser;
import com.future.identity.model.IdentityElement;

/**
 * static id parser
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class ProIdentityParser {

    /**
     * Parse id attribute
     *
     * @param id
     * @return
     */
    public static IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
