package com.codezen.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Getter
@Setter
@Entity
@Table(name = "code_reviews")
public class CodeSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String userCode;

    private String language;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String optimizedCode;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        detectLanguage();
    }

    @PreUpdate
    public void preUpdate() {
        detectLanguage();
    }

    public void detectLanguage() {
        if (userCode == null || userCode.trim().isEmpty()) {
            this.language = "unknown";
            return;
        }

        if (Pattern.compile("public static void main").matcher(userCode).find()) this.language = "java";
        else if (Pattern.compile("print\\(|def ").matcher(userCode).find()) this.language = "python";
        else if (Pattern.compile("console\\.log\\(").matcher(userCode).find()) this.language = "javascript";
        else if (Pattern.compile("#include <").matcher(userCode).find()) this.language = "cpp";
        else if (Pattern.compile("<html>").matcher(userCode).find()) this.language = "html";
        else this.language = "plaintext";
    }
}
