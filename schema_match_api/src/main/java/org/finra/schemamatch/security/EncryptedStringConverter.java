package org.finra.schemamatch.security;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.convert.converter.Converter;

/**
 * @author K24397
 */
public class EncryptedStringConverter implements Converter<String, String> {
    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
    private static final String ENCRYPTED_VALUE_SUFFIX = ")";
    private final StringEncryptor encryptor;

    public EncryptedStringConverter(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convert(String source) {
        if (StringUtils.startsWith(source, ENCRYPTED_VALUE_PREFIX) && StringUtils.endsWith(source, ENCRYPTED_VALUE_SUFFIX)) {
            return encryptor.decrypt(StringUtils.substringBetween(source,ENCRYPTED_VALUE_PREFIX,ENCRYPTED_VALUE_SUFFIX));
        }
        return source;
    }
}

