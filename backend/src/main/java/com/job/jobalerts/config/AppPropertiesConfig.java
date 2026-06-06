package com.job.jobalerts.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppPropertiesConfig {

    private Database database = new Database();
    private TapAcademy tapAcademy = new TapAcademy();
    private Sync sync = new Sync();

    @Data
    public static class Database {
        private String host;
        private int port = 5432;
        private String name;
        private String username;
        private String password;
        private int poolSize = 10;
    }

    @Data
    public static class TapAcademy {
        private String email;
        private String password;
        private String url = "https://tai.thetapacademy.com";
        private int tokenExpiryMinutes = 60;
    }

    @Data
    public static class Sync {
        private long intervalMs = 3600000; // 1 hour default
        private boolean enabled = true;
    }
}