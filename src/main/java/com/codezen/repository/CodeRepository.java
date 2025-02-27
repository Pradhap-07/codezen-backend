package com.codezen.repository;

import com.codezen.model.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<CodeSubmission, Long> {
    // Add this method to find submissions by user ID
}
