package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    // LIST
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", repo.findAll());
        return "students";
    }

    // SHOW FORM
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-form";
    }

    // SAVE
    @PostMapping
    public String saveStudent(@ModelAttribute Student student) {
        repo.save(student);
        return "redirect:/students";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", repo.findById(id).orElseThrow());
        return "student-form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/students";
    }
}