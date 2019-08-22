package org.finra.schemamatch.security;


import org.finra.pet.JCredStashFX;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.Map;

/**
 * @author K24397
 */
public class CredentialService {
    private final JCredStashFX credStash;
    private final String credStashAgs;
    private final String credStashSdlc;
    private final String credStashComponent;
    private final Map<String,String> properties;
    public CredentialService(JCredStashFX credStash, String credStashAgs, String credStashSdlc, String credStashComponent
            , Map<String,String> properties) {
        this.credStash = credStash;
        this.credStashAgs = credStashAgs;
        this.credStashSdlc = credStashSdlc;
        this.credStashComponent = credStashComponent;
        this.properties = properties;
    }

    @Cacheable(value = "getCredential")
    @Retryable(backoff = @Backoff(delay = 5000))
    public String getCredential(String key){
        try {
            if(properties.containsKey(key)){
                return properties.get(key);
            }
            return credStash.getCredential(key, credStashAgs, credStashSdlc, credStashComponent, null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to retrieve credential for given key");
        }
    }
}
