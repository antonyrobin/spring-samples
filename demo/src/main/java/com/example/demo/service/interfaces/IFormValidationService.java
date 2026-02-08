package com.example.demo.service.interfaces;

import com.example.demo.model.FormControl;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IFormValidationService {
    Map<String, String> validate(Map<String, String[]> params, Map<String, MultipartFile> files, List<FormControl> controls, boolean isEdit);
    Model handleValidationErrors(Model model, Map<String, String> errors, Map<String, String[]> params, List<FormControl> controls);
}
