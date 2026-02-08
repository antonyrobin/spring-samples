package com.example.demo.exception;

import com.example.demo.service.FormDefinitionService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MultipartExceptionAdvice {
    private final Logger log = LoggerFactory.getLogger(MultipartExceptionAdvice.class);

    @Autowired
    private FormDefinitionService formDefService;

    // Catch both Size and general Multipart errors (like the count error you saw)
    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public ModelAndView handleMultipartError(Exception ex, HttpServletRequest request) {
        log.warn("Multipart error caught: {}", ex.getMessage());

        // Use ModelAndView instead of passing Model in the method signature
        ModelAndView mav = new ModelAndView("dynamic-form");

        // 1. RE-FILL FORM STRUCTURE (Essential for Thymeleaf loop)
        mav.addObject("formControls", formDefService.getFormControls());

        // 2. DEFINE ERROR MESSAGE
        String errorMessage = "An error occurred during upload.";
        if (ex instanceof MaxUploadSizeExceededException) {
            errorMessage = "File too large. Maximum allowed size is 10MB.";
        } else if (ex.getMessage().contains("FileCountLimitExceeded")) {
            errorMessage = "Too many files/fields in the request.";
        }
        mav.addObject("error", errorMessage);

        // 3. ATTEMPT DATA RECOVERY
        // Note: request.getParameterMap() may trigger the exception again if lazy resolution is on.
        // We wrap it in a try-catch to be safe.
        try {
            Map<String, String> formData = new HashMap<>();
            request.getParameterMap().forEach((key, values) -> {
                if (values != null && values.length > 0) formData.put(key, values[0]);
            });
            mav.addObject("formData", formData);
        } catch (Exception e) {
            log.debug("Could not recover form data: {}", e.getMessage());
        }

        // 4. HANDLE EDIT VS NEW
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("/edit")) {
            mav.addObject("isEdit", true);
        }

        return mav;
    }
}