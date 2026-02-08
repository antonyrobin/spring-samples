package com.example.demo.repository.interfaces;

import com.example.demo.model.FormSubmission;
import java.util.List;
import java.util.Optional;

public interface IFormSubmissionRepository {
    List<FormSubmission> findAll();
    Optional<FormSubmission> findById(String id);
    void save(FormSubmission submission);
    void update(FormSubmission submission);
    void deleteById(String id);
}