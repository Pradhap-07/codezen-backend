package com.codezen.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor // ✅ Ensures JSON deserialization works
public class CodeSubmissionDTO {
    private String userCode;
}
