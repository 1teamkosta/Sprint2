package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.tag-type")
public record TagType(int genre, int target) {
}
