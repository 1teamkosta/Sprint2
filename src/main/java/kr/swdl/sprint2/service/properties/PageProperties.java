package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.pagination")
public record PageProperties(int pageSize) {
}
