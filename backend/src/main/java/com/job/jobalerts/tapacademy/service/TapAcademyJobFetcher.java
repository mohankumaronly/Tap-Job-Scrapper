package com.job.jobalerts.tapacademy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.jobalerts.tapacademy.client.TapAcademyClient;
import com.job.jobalerts.tapacademy.dto.TapJobDTO;
import com.job.jobalerts.tapacademy.dto.TapJobsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class TapAcademyJobFetcher {

    private final TapAcademyAuthService authService;
    private final TapAcademyClient tapAcademyClient;
    private final ObjectMapper objectMapper;

    @Value("${tap.academy.url:https://tai.thetapacademy.com}")
    private String baseUrl;

    @Value("${tap.academy.job.limit:5}")
    private int jobLimit;

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 5000; // 5 seconds

    public TapJobsResponse fetchJobs() {
        log.info("Starting to fetch and parse jobs from Tap Academy");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.info("Attempt {}/{} to fetch jobs", attempt, MAX_RETRIES);

                String cookies = authService.login();
                log.info("Got cookies, fetching jobs...");

                String rawJson = tapAcademyClient.fetchJobsAsRawJson(baseUrl, cookies, jobLimit);

                if (rawJson == null || rawJson.isEmpty()) {
                    throw new RuntimeException("No jobs received from Tap Academy");
                }

                TapJobsResponse response = objectMapper.readValue(rawJson, TapJobsResponse.class);

                if (response != null && response.getData() != null) {
                    log.info("Successfully parsed {} jobs", response.getData().size());

                    if (!response.getData().isEmpty()) {
                        TapJobDTO firstJob = response.getData().get(0);

                        // Get first location safely
                        String firstLocation = "Unknown";
                        if (firstJob.getJobLocation() != null && !firstJob.getJobLocation().isEmpty()) {
                            firstLocation = firstJob.getJobLocation().get(0);
                        }

                        log.info("Sample job - Title: {}, Role: {}, Location: {}",
                                firstJob.getJobTitle(),
                                firstJob.getJobRole(),
                                firstLocation);
                    }
                } else {
                    log.warn("No jobs found in response");
                    response = new TapJobsResponse();
                    response.setData(Collections.emptyList());
                    response.setCount(0);
                }

                log.info("Successfully fetched jobs on attempt {}", attempt);
                return response;

            } catch (Exception e) {
                log.error("Attempt {} failed: {}", attempt, e.getMessage());

                if (attempt == MAX_RETRIES) {
                    log.error("All {} attempts failed", MAX_RETRIES);
                    throw new RuntimeException("Failed to fetch jobs after " + MAX_RETRIES + " attempts: " + e.getMessage(), e);
                }

                try {
                    log.info("Waiting {} ms before retry...", RETRY_DELAY_MS);
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }

        throw new RuntimeException("Failed to fetch jobs");
    }

    public String fetchJobsAsRawJson() {
        log.info("Starting to fetch jobs from Tap Academy");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.info("Attempt {}/{} to fetch raw jobs", attempt, MAX_RETRIES);

                String cookies = authService.login();
                log.info("Got cookies, fetching jobs...");

                String rawJson = tapAcademyClient.fetchJobsAsRawJson(baseUrl, cookies, jobLimit);

                if (rawJson == null || rawJson.isEmpty()) {
                    throw new RuntimeException("No jobs received from Tap Academy");
                }

                log.info("Successfully fetched jobs on attempt {}", attempt);
                log.info("Raw JSON response length: {}", rawJson.length());

                return rawJson;

            } catch (Exception e) {
                log.error("Attempt {} failed: {}", attempt, e.getMessage());

                if (attempt == MAX_RETRIES) {
                    throw new RuntimeException("Failed to fetch jobs after " + MAX_RETRIES + " attempts: " + e.getMessage(), e);
                }

                try {
                    log.info("Waiting {} ms before retry...", RETRY_DELAY_MS);
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }

        throw new RuntimeException("Failed to fetch jobs");
    }
}