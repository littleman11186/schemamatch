package org.finra.schemamatch.database;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("schemamatch.db")
@SuppressWarnings("unused")
public class DatabaseConnections {
    private String[] urlList;
    private String[] usernameList;
    private String[] passwordList;
    private String[] driverList;

    public String[] getUrlList() {
        return urlList;
    }

    public void setUrlList(String[] urlList) {
        this.urlList = urlList;
    }

    public String[] getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(String[] usernameList) {
        this.usernameList = usernameList;
    }

    public String[] getPasswordList() {
        return passwordList;
    }

    public void setPasswordList(String[] passwordList) {
        this.passwordList = passwordList;
    }

    public String[] getDriverList() {
        return driverList;
    }

    public void setDriverList(String[] driverList) {
        this.driverList = driverList;
    }
}
