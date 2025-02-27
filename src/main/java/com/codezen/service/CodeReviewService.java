package com.codezen.service;

import com.codezen.model.CodeSubmission;
import com.codezen.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodeReviewService {

    private static final Logger logger = LoggerFactory.getLogger(CodeReviewService.class);

    @Value("${cohere.api.key}")
    private String apiKey;

    private static final String AI_API_URL = "https://api.cohere.ai/v1/generate";
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Map<String, String> requestCache = new ConcurrentHashMap<>();

    @Autowired
    private CodeRepository codeRepository;

    @PostConstruct
    public void init() {
        if (apiKey == null || apiKey.isBlank()) {
            logger.error("❌ API Key is missing! Check application.properties");
        } else {
            logger.info("🔵 API Key Loaded Successfully");
        }
    }

    public CodeSubmission reviewCode(CodeSubmission submission) {
        if (submission == null || submission.getUserCode() == null || submission.getUserCode().trim().isEmpty()) {
            logger.warn("⚠️ Code submission is empty. Rejecting request.");
            throw new IllegalArgumentException("Code submission cannot be empty.");
        }

        String userCode = submission.getUserCode();
        String detectedLanguage = submission.getLanguage(); // Fetching language from CodeSubmission model
        logger.info("🟢 Detected Language: {}", detectedLanguage);

        // 🔹 Caching to Avoid Duplicate API Calls
        if (requestCache.containsKey(userCode)) {
            submission.setAiFeedback(requestCache.get(userCode));
        } else {
            logger.info("🟢 Sending Code to AI for Review...");
            String aiFeedback = callCohere("Analyze and optimize this " + detectedLanguage + " code:\n" + userCode);
            requestCache.put(userCode, aiFeedback);
            submission.setAiFeedback(aiFeedback);
        }

        submission.setOptimizedCode(userCode.replaceAll("\\s+", " ")); // Simple space normalization

        try {
            return codeRepository.save(submission);
        } catch (Exception e) {
            logger.error("❌ Database save failed: {}", e.getMessage(), e);
            return null;
        }
    }

    private String callCohere(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            logger.error("❌ API key is missing or invalid.");
            return "Error: API key is missing.";
        }

        if (prompt == null || prompt.trim().isEmpty()) {
            logger.error("❌ Prompt is empty. Cannot send request.");
            return "Error: Prompt cannot be empty.";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "model", "command",
                "prompt", prompt,
                "max_tokens", 4096,
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            logger.info("🔵 Sending request to Cohere...");
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    AI_API_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});

            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error("❌ AI API returned unexpected status: {}", response.getStatusCode());
                return "Error: AI API returned status " + response.getStatusCode();
            }

            Map<String, Object> responseBody = response.getBody();
            return extractAIResponse(responseBody);

        } catch (Exception e) {
            logger.error("❌ API call failed: {}", e.getMessage(), e);
            return "Error: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }

    private String extractAIResponse(Map<String, Object> responseBody) {
        if (responseBody == null || !responseBody.containsKey("generations")) {
            return "Error: AI response was empty.";
        }

        Object generationsObj = responseBody.get("generations");
        if (generationsObj instanceof List<?> generationsList) {
            for (Object item : generationsList) {
                if (item instanceof Map<?, ?> generationMap && generationMap.containsKey("text")) {
                    return generationMap.get("text").toString();
                }
            }
        }

        return "Error: No valid AI response.";
    }

    public List<CodeSubmission> getAllReviews() {
        return codeRepository.findAll();
    }



}
