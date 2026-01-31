package com.example.demo.model;

import java.util.Map;
import java.util.UUID;

public class FormSubmission {
    private String id;
    private Map<String, String> textData;
    private Map<String, String> fileData; // Stores filename

    public FormSubmission(Map<String, String> textData, Map<String, String> fileData) {
        this.id = UUID.randomUUID().toString();
        this.textData = textData;
        this.fileData = fileData;
    }

    public String getId() { return id; }
    public Map<String, String> getTextData() { return textData; }
    public Map<String, String> getFileData() { return fileData; }
}