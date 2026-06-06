package com.job.jobalerts.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigurationValidator {

    private final Environment environment;

    @PostConstruct
    public void validateConfiguration() {
        log.info("🔍 Validating application configuration...");

        List<RequiredProperty> requiredProps = Arrays.asList(
                new RequiredProperty("spring.datasource.url", "Database URL"),
                new RequiredProperty("spring.datasource.username", "Database Username"),
                new RequiredProperty("spring.datasource.password", "Database Password")
        );

        boolean allValid = true;

        for (RequiredProperty prop : requiredProps) {
            String value = environment.getProperty(prop.key);
            if (value == null || value.isEmpty()) {
                log.error("❌ Missing required configuration: {} ({})", prop.name, prop.key);
                allValid = false;
            } else {
                // Mask sensitive values
                String maskedValue = prop.key.contains("password") ? "****" : value;
                log.info("✅ {}: {}", prop.name, maskedValue);
            }
        }

        // Check Tap Academy configuration (optional but recommended)
        String tapEmail = environment.getProperty("tap.academy.email");
        if (tapEmail == null || tapEmail.isEmpty()) {
            log.warn("⚠️ Tap Academy email not configured - job sync will be disabled");
        } else {
            log.info("✅ Tap Academy configured for: {}", tapEmail);
        }

        if (!allValid) {
            log.error("❌ Application may not function correctly due to missing configuration");
            log.error("Please check your .env file or environment variables");
        } else {
            log.info("✅ All required configurations are valid!");
        }
    }

    private record RequiredProperty(String key, String name) {}
}