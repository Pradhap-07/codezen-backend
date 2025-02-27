package com.codezen.controller;

import com.codezen.dto.CodeSubmissionDTO;
import com.codezen.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/code-submissions")
public class CodeSubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(CodeSubmissionController.class);

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitCode(@RequestBody CodeSubmissionDTO submissionDTO) {
        try {
            // Validate input data
            if (submissionDTO == null || submissionDTO.getUserCode() == null || submissionDTO.getUserCode().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "⚠️ Code submission cannot be empty."));
            }

            // Save to database
            codeSubmissionService.saveSubmission(submissionDTO);
            return ResponseEntity.ok(Map.of("message", "✅ Code submitted successfully!", "status", "success"));
        } catch (Exception e) {
            logger.error("❌ Error submitting code: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Internal Server Error", "details", e.getMessage()));
        }
    }
}
