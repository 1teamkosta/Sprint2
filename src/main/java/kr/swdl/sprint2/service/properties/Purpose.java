package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.purpose")
public record Purpose(String festival, String food) {
}
