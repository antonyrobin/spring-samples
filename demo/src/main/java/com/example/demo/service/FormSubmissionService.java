package com.example.demo.service;

import com.example.demo.model.FormSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormSubmissionService {
    @Autowired
    private FileStorageService fileStorageService;
    // In-memory database
    private final List<FormSubmission> repository = new ArrayList<>();

    public void save(FormSubmission submission) {
        repository.add(submission);
    }

    public void update(String id, Map<String, String[]> params, Map<String, MultipartFile> files) {
        FormSubmission existing = getById(id);
        if (existing != null) {
            // Update Text Data
            Map<String, String> newTextData = new HashMap<>();
            params.forEach((k, v) -> {
                if (!k.startsWith("_")) { // Skip internal Spring/CSRF fields
                    newTextData.put(k, v[0]);
                }
            });

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
        }
    }

    public List<FormSubmission> getAll() {
        return new ArrayList<>(repository); // Return copy
    }

    public FormSubmission getById(String id) {
        return repository.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void delete(String id) {
        repository.removeIf(s -> s.getId().equals(id));
    }
}