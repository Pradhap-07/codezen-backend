package com.codezen.service;

import com.codezen.dto.CodeSubmissionDTO;
import com.codezen.model.CodeSubmission;
import com.codezen.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private CodeReviewService codeReviewService; // Inject AI Review Service

    public void saveSubmission(CodeSubmissionDTO submissionDTO) {
        CodeSubmission submission = new CodeSubmission();
        submission.setUserCode(submissionDTO.getUserCode());
        submission.detectLanguage(); // Detect language

        // Perform AI Review and store feedback
        CodeSubmission reviewedSubmission = codeReviewService.reviewCode(submission);
        codeRepository.save(reviewedSubmission);
    }


}
