package com.job.jobalerts.tapacademy.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TapAcademyConfig {

    @Value("${tap.academy.email:}")
    private String email;

    @Value("${tap.academy.password:}")
    private String password;

    @Value("${tap.academy.url:https://tai.thetapacademy.com}")
    private String baseUrl;
}