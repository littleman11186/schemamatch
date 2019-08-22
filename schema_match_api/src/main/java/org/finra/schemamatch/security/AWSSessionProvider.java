package org.finra.schemamatch.security;

import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AWSSessionProvider implements AWSSessionCredentialsProvider {


    private Logger log = LoggerFactory.getLogger(this.getClass());

    String username;
    String password;
    String domain;
    String roleProviderARN;
    String roleToAssumeARN;
    String cloudPassUrl;

    private String accessKey;
    private String secretKey;
    private String sessionToken;
    private Date awsExpirationDate;
    private BasicSessionCredentials sessionCreadentials;
    private RestTemplate restTemplate;
    private final boolean useEnv;
    //private ObjectMapper mapper = new ObjectMapper();

    public AWSSessionProvider(String username, String password, String domain, String roleProviderARN, String roleToAssumeARN, String cloudPassUrl, RestTemplate restTemplate)
    {
        this(username,password,domain,roleProviderARN,roleToAssumeARN,cloudPassUrl,restTemplate,false);
    }

    public AWSSessionProvider(String username, String password, String domain, String roleProviderARN, String roleToAssumeARN, String cloudPassUrl, RestTemplate restTemplate, boolean useEnv) {
        this.username = username;
        this.password = password;
        this.domain = domain;
        this.roleProviderARN = roleProviderARN;
        this.roleToAssumeARN = roleToAssumeARN;
        this.cloudPassUrl = cloudPassUrl;
        this.restTemplate = restTemplate;
        this.useEnv = useEnv;
    }

    private void createAwsSession() throws Exception
    {
        Map<String,String> request = new HashMap<>();
        request.put("username",username);
        request.put("password", password);
        request.put("domain", domain);
        request.put("roleProviderARN", roleProviderARN);
        request.put("roleToAssumeARN", roleToAssumeARN);


        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map aws = restTemplate.postForEntity(cloudPassUrl, new HttpEntity<Object>(request, headers), Map.class).getBody();

            this.accessKey = (String) aws.get("awsAccessKeyId");
            this.secretKey = (String) aws.get("awsSecretKey");
            this.sessionToken = (String) aws.get("awsSessionToken");
            this.awsExpirationDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);

            log.debug("AWS Session established.");
            this.sessionCreadentials = new BasicSessionCredentials(this.accessKey, this.secretKey,this.sessionToken);

        } catch (Exception e) {
            log.debug("Error to establish AWS Session.", e);
            throw e;
        }

    }

    private boolean isSessionExpired()
    {
        if(this.awsExpirationDate != null && System.currentTimeMillis() < this.awsExpirationDate.getTime()) {
            return false;
        } else {
            return true;
        }
    }

    public BasicSessionCredentials getSessionCreadentials() {
        if(isSessionExpired()){
            try {
                createAwsSession();
            } catch (Exception e) {
                log.error("Failed to create AWS session",e);
                return null;
            }
        }
        return this.sessionCreadentials;
    }

    @Override
    public AWSSessionCredentials getCredentials() {
        if(useEnv){
            return getSessionCredentialsEnv();
        }
        return getSessionCreadentials();
    }

    private AWSSessionCredentials getSessionCredentialsEnv() {
        if(this.sessionCreadentials != null){
            return this.sessionCreadentials;
        }
//        String awsAccessKey = System.getenv().get("AWS_ACCESS_KEY_ID");
//        String awsSecretKey = System.getenv().get("AWS_SECRET_ACCESS_KEY");
//        String awsSecurityToken = System.getenv().get("AWS_SECURITY_TOKEN");
//        this.sessionCreadentials = new BasicSessionCredentials(awsAccessKey, awsSecretKey,awsSecurityToken);
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        this.sessionCreadentials = (BasicSessionCredentials) (credentialsProvider.getCredentials());
        return this.sessionCreadentials;
    }

    @Override
    public void refresh() {
        if(useEnv){
            getSessionCredentialsEnv();
        }else {
            try {
                createAwsSession();
            } catch (Exception e) {
                log.error("Failed to create AWS session", e);
            }
        }
    }
}

