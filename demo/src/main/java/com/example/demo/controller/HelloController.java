package com.example.demo.controller;

import com.example.demo.model.SpringModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String index() {
        SpringModel model = new SpringModel("Hello, World!");
        model.setName("Spring User");
        model.setSuccess(true);
        return "Greetings from Spring Boot! <br /><a href='model'>Go to /model</a> <br /> <a href='/students'>Go to /students</a>";
    }

    @GetMapping("/model")
    public SpringModel model() {
        SpringModel model = new SpringModel("Hello, World!");
        model.setName("Spring User");
        model.setSuccess(true);
        return model;
    }

}