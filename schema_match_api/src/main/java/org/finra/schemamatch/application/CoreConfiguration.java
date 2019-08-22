package org.finra.schemamatch.application;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import org.finra.pet.JCredStashFX;
import org.finra.schemamatch.security.AWSSessionProvider;
import org.finra.schemamatch.security.CredentialService;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CoreConfiguration {
    @Bean(name = "credentialService")
    public CredentialService credentialService(@Value("${esp.credstash.ags}") String credstashAgs,
                                               @Value("${esp.credstash.sdlc}") String credstashSdlc,
                                               @Value("${esp.credstash.component}") String credstashComponent,
                                               @Value("${dplr.aws.useProxy:false}") boolean useProxy,
                                               @Value("${dplr.useEnv:false}") boolean useEnv,
                                               AWSSessionProvider awsSessionManager) {
        Map<String, String> maps = new HashMap<>();
        if(useEnv) {
            System.getProperties().forEach((key, value) -> maps.put(key.toString(), value.toString()));
            maps.putAll(System.getenv());
        }
        if (useProxy) {
            ClientConfiguration clientConf = new ClientConfiguration();
            return new CredentialService(new JCredStashFX(clientConf, awsSessionManager, Regions.US_EAST_1.getName()),
                    credstashAgs, credstashSdlc, credstashComponent, maps);
        } else {
            return new CredentialService(new JCredStashFX(), credstashAgs, credstashSdlc
                    , credstashComponent, maps);
        }
    }

    @Bean
    public StringEncryptor encryptor(CredentialService credentialService) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        String password = credentialService.getCredential("APP_ENCRYPTION_PASSWORD");
        config.setPassword(password);
        encryptor.setConfig(config);
        return encryptor;
    }

    @Bean
    public AWSSessionProvider awsSessionProvider(@Value("${cloudpass.username:none}") String username,
                                                 @Value("${cloudpass.password:none}") String password,
                                                 @Value("${cloudpass.domain:NASDCORP}") String domain,
                                                 @Value("${cloudpass.roleProviderARN:none}") String roleProviderARN,
                                                 @Value("${cloudpass.roleToAssumeARN:none}") String roleToAssumeARN,
                                                 @Value("${cloudpass.url:https://cloudpass.finra.org/cloudpass/api/v1/getToken}") String url,
                                                 @Value("${cloudpass.useEnv:false}") boolean useEnv) {
        return new AWSSessionProvider(username, password, domain, roleProviderARN, roleToAssumeARN, url, new RestTemplate(), useEnv);
    }
}

