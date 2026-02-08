package com.example.demo.service;

import com.example.demo.model.FormControl;
import com.example.demo.service.interfaces.IFormValidationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class FormValidationService implements IFormValidationService {

    @Override
    public Map<String, String> validate(Map<String, String[]> params, Map<String, MultipartFile> files, List<FormControl> controls, boolean isEdit) {
        Map<String, String> errors = new HashMap<>();

        for (FormControl control : controls) {
            String name = control.getName();

            // File Validation
            if (control.getType().equals("file") || control.getType().equals("image")) {
                boolean hasNewFile = files.containsKey(name) && !files.get(name).isEmpty();

                if (control.isMandatory() && !hasNewFile && !isEdit) {
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
            String[] values = params.get(name);
            String value = (values != null && values.length > 0) ? String.join(",", values) : "";

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

    @Override
    public Model handleValidationErrors(Model model, Map<String, String> errors, Map<String, String[]> params, List<FormControl> controls) {
        model.addAttribute("errors", errors);
        model.addAttribute("formControls", controls);

        // Retain values (simple implementation)
        Map<String, String> retained = new HashMap<>();
        params.forEach((k, v) -> retained.put(k, String.join(",", v)));
        model.addAttribute("formData", retained);

        return model;
    }
}