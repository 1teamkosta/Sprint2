package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.account-role")
public record AccountRole (AccountSetting user, AccountSetting admin) {
    public record AccountSetting (int code, String authority) {}
}

