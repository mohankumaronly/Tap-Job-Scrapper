package com.job.jobalerts.tapacademy.service;

import com.job.jobalerts.tapacademy.client.TapAcademyClient;
import com.job.jobalerts.tapacademy.dto.TapLoginRequest;
import com.job.jobalerts.tapacademy.dto.TapLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TapAcademyAuthService {

    private final TapAcademyClient tapAcademyClient;

    @Value("${tap.academy.email:}")
    private String email;

    @Value("${tap.academy.password:}")
    private String password;

    @Value("${tap.academy.url:https://tai.thetapacademy.com}")
    private String baseUrl;

    // Cache cookies in memory
    private String cachedCookies;
    private long cookieExpiryTime;

    public String getValidCookies() {
        // Check if we have valid cached cookies
        if (cachedCookies != null && !cachedCookies.isEmpty() && System.currentTimeMillis() < cookieExpiryTime) {
            log.debug("Using cached cookies");
            return cachedCookies;
        }

        // Login to get new cookies
        return login();  // Now calling public login() method
    }

    // Changed from private to public
    public String login() {
        log.info("Attempting to login to Tap Academy");

        // Validate credentials
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Tap Academy email not configured in .env file");
        }
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Tap Academy password not configured in .env file");
        }

        log.info("Using email: {}", maskEmail(email));
        log.info("Using URL: {}", baseUrl);

        // Create login request
        TapLoginRequest loginRequest = TapLoginRequest.builder()
                .email(email)
                .password(password)
                .username(email)
                .build();

        String loginUrl = baseUrl + "/api/v1/auth/login";

        ResponseEntity<TapLoginResponse> response = tapAcademyClient.login(loginUrl, loginRequest);

        // Log response body
        log.info("Response Status: {}", response.getStatusCode());
        if (response.getBody() != null) {
            log.info("User ID from response: {}", response.getBody().getUser());
        }

        // Extract cookies
        String cookies = extractCookies(response);

        // cookies will never be null from extractCookies() - it returns "" if none found
        if (cookies.isEmpty()) {
            throw new RuntimeException("Login failed - no cookies received");
        }

        // Cache cookies (assuming 1 hour expiry)
        cachedCookies = cookies;
        cookieExpiryTime = System.currentTimeMillis() + 3600000;

        log.info("Successfully logged in to Tap Academy - cookies cached for 1 hour");
        return cookies;
    }

    private String extractCookies(ResponseEntity<TapLoginResponse> response) {
        HttpHeaders headers = response.getHeaders();

        var cookies = headers.get(HttpHeaders.SET_COOKIE);

        if (cookies == null || cookies.isEmpty()) {
            log.warn("No Set-Cookie headers received");
            log.warn("Available headers: {}", headers.keySet());
            return "";
        }

        log.info("Found {} cookies", cookies.size());  // Don't log full cookies

        StringBuilder cookieString = new StringBuilder();
        for (String cookie : cookies) {
            String cookiePart = cookie.split(";")[0];
            if (cookieString.length() > 0) {
                cookieString.append("; ");
            }
            cookieString.append(cookiePart);
            // For development only - remove in production
            log.debug("Cookie: {}", cookiePart.length() > 20 ? cookiePart.substring(0, 20) + "..." : cookiePart);
        }

        return cookieString.toString();
    }

    private String maskEmail(String email) {
        if (email == null || email.length() < 3) return "***";
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) return "***" + email.substring(atIndex);
        return email.substring(0, 2) + "***" + email.substring(atIndex);
    }
}