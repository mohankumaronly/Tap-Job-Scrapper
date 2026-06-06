package com.job.jobalerts.tapacademy.controller;

import com.job.jobalerts.tapacademy.dto.TapJobDTO;
import com.job.jobalerts.tapacademy.dto.TapJobsResponse;
import com.job.jobalerts.tapacademy.service.TapAcademyAuthService;
import com.job.jobalerts.tapacademy.service.TapAcademyJobFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tap")
@RequiredArgsConstructor
public class TapAcademyTestController {

    private final TapAcademyAuthService tapAcademyAuthService;
    private final TapAcademyJobFetcher tapAcademyJobFetcher;

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> testLogin() {
        log.info("GET /api/tap/login - Testing Tap Academy authentication");

        try {
            String cookies = tapAcademyAuthService.login();

            if (!cookies.isEmpty()) {
                log.info("Login successful - cookies obtained");
                Map<String, String> response = new HashMap<>();
                response.put("status", "SUCCESS");
                response.put("message", "Login successful - cookies captured");
                response.put("cookieCount", String.valueOf(cookies.split(";").length));
                return ResponseEntity.ok(response);
            } else {
                log.warn("Login returned but no cookies received");
                Map<String, String> response = new HashMap<>();
                response.put("status", "PARTIAL");
                response.put("message", "Login successful but no cookies received");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("Login failed", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "FAILED");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/jobs-raw")
    public ResponseEntity<Map<String, String>> testFetchJobsRaw() {
        log.info("GET /api/tap/jobs-raw - Testing raw job fetching");

        try {
            String rawJson = tapAcademyJobFetcher.fetchJobsAsRawJson();

            if (rawJson == null || rawJson.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "ERROR");
                response.put("message", "No jobs received");
                return ResponseEntity.status(500).body(response);
            }

            // Only return preview, not full JSON to avoid huge responses
            int previewLength = Math.min(500, rawJson.length());
            String preview = rawJson.substring(0, previewLength);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Raw JSON fetched successfully");
            response.put("length", String.valueOf(rawJson.length()));
            response.put("preview", preview + (rawJson.length() > 500 ? "..." : ""));
            response.put("note", "Full JSON is logged in application logs");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Job fetch failed", e);
            Map<String, String> response = new HashMap<>();
            response.put("status", "FAILED");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<Map<String, String>> testFetchParsedJobs() {
        log.info("GET /api/tap/jobs - Testing parsed job fetching");

        try {
            TapJobsResponse response = tapAcademyJobFetcher.fetchJobs();

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                Map<String, String> result = new HashMap<>();
                result.put("status", "SUCCESS");
                result.put("message", "No jobs found");
                result.put("count", "0");
                return ResponseEntity.ok(result);
            }

            TapJobDTO firstJob = response.getData().get(0);

            // Get first location safely
            String firstLocation = "Unknown";
            if (firstJob.getJobLocation() != null && !firstJob.getJobLocation().isEmpty()) {
                firstLocation = firstJob.getJobLocation().get(0);
            }

            Map<String, String> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Jobs fetched and parsed successfully");
            result.put("count", String.valueOf(response.getCount()));
            result.put("firstJobTitle", firstJob.getJobTitle() != null ? firstJob.getJobTitle() : "N/A");
            result.put("firstJobRole", firstJob.getJobRole() != null ? firstJob.getJobRole() : "N/A");
            result.put("firstJobLocation", firstLocation);
            result.put("firstJobPackage", firstJob.getPackageLpa() != null ? String.valueOf(firstJob.getPackageLpa()) : "N/A");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Job fetch failed", e);
            Map<String, String> result = new HashMap<>();
            result.put("status", "FAILED");
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}