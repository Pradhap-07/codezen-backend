package com.codezen.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {

    @NotBlank(message = "Feedback cannot be empty.") // ✅ Prevents empty feedback
    private String feedback;
}
