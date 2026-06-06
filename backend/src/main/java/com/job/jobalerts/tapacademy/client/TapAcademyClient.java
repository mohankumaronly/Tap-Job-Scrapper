package com.job.jobalerts.tapacademy.client;

import com.job.jobalerts.tapacademy.dto.TapLoginRequest;
import com.job.jobalerts.tapacademy.dto.TapLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class TapAcademyClient {

    private final RestTemplate restTemplate;

    public TapAcademyClient() {
        // Create custom RestTemplate with longer timeouts
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000);      // 60 seconds connection timeout
        factory.setReadTimeout(60000);         // 60 seconds read timeout
        this.restTemplate = new RestTemplate(factory);
    }

    public ResponseEntity<TapLoginResponse> login(String url, TapLoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Origin", "https://tai.thetapacademy.com");
            headers.set("Referer", "https://tai.thetapacademy.com/loginEmail");
            headers.set("Accept", "application/json, text/plain, */*");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            HttpEntity<TapLoginRequest> requestEntity = new HttpEntity<>(request, headers);

            log.info("Calling Tap Academy login at: {}", url);

            ResponseEntity<TapLoginResponse> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    TapLoginResponse.class
            );

            log.info("Response Status: {}", response.getStatusCode());
            log.info("Response Headers: {}", response.getHeaders());

            return response;
        } catch (Exception e) {
            log.error("Failed to call Tap Academy login API", e);
            throw new RuntimeException("Failed to call Tap Academy login API: " + e.getMessage(), e);
        }
    }

    public String fetchJobsAsRawJson(String baseUrl, String cookies, int limit) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .path("/api/v1/drivesGeneral/get-active-drives")
                    .queryParam("filters[isTechnical]", "true")
                    .queryParam("limit", limit)
                    .build()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", cookies);
            headers.set("Accept", "application/json, text/plain, */*");
            headers.set("Origin", "https://tai.thetapacademy.com");
            headers.set("Referer", "https://tai.thetapacademy.com/loginEmail");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("Fetching jobs from: {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("Jobs fetch response status: {}", response.getStatusCode());

            String rawJson = response.getBody();
            if (rawJson != null) {
                log.info("Raw JSON response length: {} characters", rawJson.length());
                if (rawJson.length() > 500) {
                    log.info("Raw JSON preview: {}", rawJson.substring(0, 500) + "...");
                } else {
                    log.info("Raw JSON response: {}", rawJson);
                }
            }

            return rawJson;

        } catch (Exception e) {
            log.error("Failed to fetch jobs", e);
            throw new RuntimeException("Failed to fetch jobs: " + e.getMessage(), e);
        }
    }
}