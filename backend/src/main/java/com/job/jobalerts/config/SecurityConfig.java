package com.job.jobalerts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/health",
                                "/api/tap/login",
                                "/api/tap/jobs-raw",
                                "/api/tap/jobs",
                                "/api/jobs",
                                "/api/jobs/**",
                                "/api/auth/send-otp",
                                "/api/auth/verify-otp",
                                "/api/auth/unsubscribe"
                        ).permitAll()
                        .anyRequest().permitAll()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}