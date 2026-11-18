package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.user-status")
public record UserStatus(int banned, int active) {
}
