package com.example.demo.model;

import java.util.Map;
import java.util.UUID;

public class FormSubmission {
    private String id;
    private Map<String, Object> textData;
    private Map<String, String> fileData; // Stores filename

    public FormSubmission(Map<String, Object> textData, Map<String, String> fileData) {
        this.id = UUID.randomUUID().toString();
        this.textData = textData;
        this.fileData = fileData;
    }

    public String getId() { return id; }
    public Map<String, Object> getTextData() { return textData; }
    public Map<String, String> getFileData() { return fileData; }
}