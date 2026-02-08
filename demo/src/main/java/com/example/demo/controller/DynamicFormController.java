package com.example.demo.controller;

import com.example.demo.model.FormControl;
import com.example.demo.model.FormSubmission;
import com.example.demo.service.interfaces.IFileStorageService;
import com.example.demo.service.interfaces.IFormDefinitionService;
import com.example.demo.service.interfaces.IFormSubmissionService;
import com.example.demo.service.interfaces.IFormValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class DynamicFormController {

    @Autowired private IFormDefinitionService formDefService;
    @Autowired private IFormValidationService validationService;
    @Autowired private IFormSubmissionService submissionService;
    @Autowired private IFileStorageService fileStorageService;

    // 1. Dashboard (Table View)
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("submissions", submissionService.getAll());
        return "dashboard";
    }

    // 2. Show New Form
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("formControls", formDefService.getFormControls());
        model.addAttribute("isEdit", false);
        model.addAttribute("submissionId", "");
        return "dynamic-form";
    }

    // 3. Handle Submit
    @PostMapping("/submit")
    public String handleSubmit(MultipartHttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        List<FormControl> controls = formDefService.getFormControls();

        // Extract Data
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, MultipartFile> fileMap = request.getFileMap();

        // Server-Side Validation
        Map<String, String> errors = validationService.validate(paramMap, fileMap, controls, false);
        if (!errors.isEmpty()) {
            model = validationService.handleValidationErrors(model, errors, paramMap, controls);
            model.addAttribute("isEdit", false);
            model.addAttribute("submissionId", "");
            return "dynamic-form";
        }

        submissionService.save(paramMap, fileMap);

        redirectAttributes.addFlashAttribute("message", "Application submitted successfully!");
        return "redirect:";
    }

    // 4. View Detail
    @GetMapping("/view/{id}")
    public String viewSubmission(@PathVariable String id, Model model) {
        FormSubmission submission = submissionService.getById(id);
        if (submission == null) return "redirect:/";

        model.addAttribute("submission", submission);
        // We need controls to know labels
        model.addAttribute("controls", formDefService.getFormControls());
        return "view-submission";
    }

    // Show Edit Form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model) {
        FormSubmission submission = submissionService.getById(id);
        if (submission == null) return "redirect:/";

        model.addAttribute("formControls", formDefService.getFormControls());
        model.addAttribute("formData", submission.getTextData());
        model.addAttribute("submission", submission);
        model.addAttribute("isEdit", true);
        model.addAttribute("submissionId", id);
        return "dynamic-form";
    }

    // Update existing record
    @PostMapping("/update/{id}")
    public String handleUpdate(@PathVariable String id, MultipartHttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        List<FormControl> controls = formDefService.getFormControls();

        // Extract Data
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<String, MultipartFile> fileMap = request.getFileMap();

        // Server-Side Validation
        Map<String, String> errors = validationService.validate(paramMap, fileMap, controls, true);
        if (!errors.isEmpty()) {
            model = validationService.handleValidationErrors(model, errors, paramMap, controls);
            model.addAttribute("isEdit", true);
            model.addAttribute("submissionId", id);
            return "dynamic-form";
        }

        // Logic similar to handleSubmit but replaces data in ArrayList for the given ID
        submissionService.update(id, request.getParameterMap(), request.getFileMap());
        redirectAttributes.addFlashAttribute("message", "Record updated successfully!");
        return "redirect:/";
    }

    // 5. Delete
    @GetMapping("/delete/{id}")
    public String deleteSubmission(@PathVariable String id, RedirectAttributes redirectAttributes) {
        submissionService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Record deleted successfully.");
        return "redirect:/";
    }

    // 6. Download File
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = fileStorageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}