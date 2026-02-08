package com.example.demo.service.interfaces;

import com.example.demo.model.FormSubmission;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IFormSubmissionService {
    void save(Map<String, String[]> params, Map<String, MultipartFile> files);
    void update(String id, Map<String, String[]> params, Map<String, MultipartFile> files);
    List<FormSubmission> getAll();
    FormSubmission getById(String id);
    void delete(String id);
}
