package ru.greenatom.atsdb.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Setter
@Component
@ConfigurationProperties(prefix = "app.path.authentication")
public class AuthenticationUrlConfig {
    
    private String main;
    private String login;
    private String signin;
    private String refresh;

    public String getMaimUrl() {
        return this.main;
    }

    
    public String getLoginUrl() {
        return this.main + this.login;
    }

    public String getSigninUrl() {
        return this.main + this.signin;
    }

    public String getRefreshUrl() {
        return this.main + this.refresh;
    }
}
