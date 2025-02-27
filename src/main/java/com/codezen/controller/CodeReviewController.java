package com.codezen.controller;

import com.codezen.dto.CodeSubmissionDTO;
import com.codezen.model.CodeSubmission;
import com.codezen.service.CodeReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend access
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    @PostMapping("/review")
    public ResponseEntity<?> reviewCode(@Valid @RequestBody CodeSubmissionDTO submissionDTO) {
        if (submissionDTO == null || submissionDTO.getUserCode() == null || submissionDTO.getUserCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "⚠️ Code submission cannot be empty."));
        }

        CodeSubmission submission = new CodeSubmission();
        submission.setUserCode(submissionDTO.getUserCode());

        CodeSubmission reviewedCode = codeReviewService.reviewCode(submission);
        return ResponseEntity.ok(reviewedCode);
    }
}
