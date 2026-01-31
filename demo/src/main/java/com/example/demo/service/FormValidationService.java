package com.example.demo.service;

import com.example.demo.model.FormControl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FormValidationService {

    public Map<String, String> validate(Map<String, String[]> params, Map<String, MultipartFile> files, List<FormControl> controls) {
        Map<String, String> errors = new HashMap<>();

        for (FormControl control : controls) {
            String name = control.getName();

            // File Validation
            if (control.getType().equals("file") || control.getType().equals("image")) {
                if (control.isMandatory() && (!files.containsKey(name) || files.get(name).isEmpty())) {
                    errors.put(name, control.getLabel() + " is required.");
                } else if (files.containsKey(name) && !files.get(name).isEmpty()) {
                    MultipartFile file = files.get(name);
                    // Image Type Restriction
                    if (control.getType().equals("image")) {
                        String contentType = file.getContentType();
                        if (contentType == null || !contentType.startsWith("image/")) {
                            errors.put(name, "Only image files (JPG, PNG) are allowed.");
                        }
                    }
                }
                continue;
            }

            // Text/Data Validation
            String value = (params.containsKey(name) && params.get(name).length > 0) ? params.get(name)[0] : "";

            if (control.isMandatory() && (value == null || value.trim().isEmpty())) {
                errors.put(name, control.getLabel() + " is required.");
            } else if (value != null && !value.isEmpty() && control.getRegex() != null && !control.getRegex().isEmpty()) {
                if (!Pattern.matches(control.getRegex(), value)) {
                    errors.put(name, control.getValidationMessage());
                }
            }
        }
        return errors;
    }
}