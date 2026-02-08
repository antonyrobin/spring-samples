package com.example.demo.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
// Try the servlet-error path first as it's the most common in SB 3.x/4.x
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // 1. Try to get the explicit error message
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        // 2. Try to get the actual Exception object if message is null
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        String displayMessage = (message != null) ? message.toString() :
                (exception != null) ? exception.getMessage() : "Unknown Server Error";

        model.addAttribute("statusCode", status != null ? status.toString() : "500");
        model.addAttribute("message", displayMessage);

        return "error";
    }
}