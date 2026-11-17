package kr.swdl.sprint2.service.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.progress-status")
public record ProgressStatus(int closed, int progress, int upcomming) {}
