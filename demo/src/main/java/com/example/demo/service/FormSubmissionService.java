package com.example.demo.service;

import com.example.demo.model.FormSubmission;
import com.example.demo.repository.interfaces.IFormSubmissionRepository;
import com.example.demo.service.interfaces.IFileStorageService;
import com.example.demo.service.interfaces.IFormSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormSubmissionService implements IFormSubmissionService {
    @Autowired
    private IFileStorageService fileStorageService;
    // In-memory database
    @Autowired
    @Qualifier("localStore")
    private IFormSubmissionRepository submissionRepository;

    public Map<String, String> getInputTextData(Map<String, String[]> params) {
        Map<String, String> newTextData = new HashMap<>();
        params.forEach((k, v) -> {
            if (!k.startsWith("_")) { // Skip internal Spring/CSRF fields
                if (v != null && v.length > 1) {
                    newTextData.put(k, String.join(",", v));
                } else {
                    newTextData.put(k, v[0]);
                }
            }
        });
        return newTextData;
    }

    @Override
    public void save(Map<String, String[]> params, Map<String, MultipartFile> files) {
        Map<String, String> newTextData = getInputTextData(params);

        Map<String, String> savedFileData = new HashMap<>();
        // Update File Data
        files.forEach((key, file) -> {
            if (!file.isEmpty()) {
                // Store new file and update the filename reference
                String storedFileName = fileStorageService.store(file);
                savedFileData.put(key, storedFileName);
            }
        });
        FormSubmission submission = new FormSubmission(newTextData, savedFileData);
        submissionRepository.save(submission);
    }

    @Override
    public void update(String id, Map<String, String[]> params, Map<String, MultipartFile> files) {
        FormSubmission existing = getById(id);
        if (existing != null) {
            // Update Text Data
            Map<String, String> newTextData = getInputTextData(params);

            // Clear old text data and replace with new
            existing.getTextData().clear();
            existing.getTextData().putAll(newTextData);

            // Update File Data
            files.forEach((key, file) -> {
                if (!file.isEmpty()) {
                    // Store new file and update the filename reference
                    String storedFileName = fileStorageService.store(file);
                    existing.getFileData().put(key, storedFileName);
                }
            });
            submissionRepository.update(existing);
        }
    }

    @Override
    public List<FormSubmission> getAll() {
        return submissionRepository.findAll(); // Return copy
    }

    @Override
    public FormSubmission getById(String id) {
        return submissionRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {
        submissionRepository.deleteById(id);
    }
}