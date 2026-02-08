package com.example.demo.service;

import com.example.demo.model.FormControl;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FormDefinitionService {
    public List<FormControl> getFormControls() {
        List<FormControl> controls = new ArrayList<>();
        // 1. Text
        controls.add(new FormControl("text", "fullName", "Full Name", true, "^[a-zA-Z\\s]+$", "Only letters allowed"));
        // 2. Email
        controls.add(new FormControl("email", "emailAddress", "Email Address", true, "^[A-Za-z0-9+_.-]+@(.+)$", "Invalid email format"));
        controls.add(new FormControl("tel", "mobile", "Mobile", false, "", ""));
        // 3. Profile Picture (New Control Type)
        controls.add(new FormControl("image", "profilePic", "Profile Picture (JPG/PNG)", true, "", ""));
        // 4. Skills (Tagify)
        controls.add(new FormControl("select-multiple", "skills", "Skills", true, "", "", "Java", "Python", "Spring"));
        // 5. Bio (Rich Text)
        controls.add(new FormControl("richtext", "bio", "Professional Bio", false, "", ""));
        // 6. Resume
        controls.add(new FormControl("file", "resume", "Upload Resume", false, "", ""));
        // 7. Experience
        controls.add(new FormControl("radio", "experience", "Experience Level", true, "", "", "Junior", "Senior"));

        controls.add(new FormControl("checkbox", "ready", "Willing to Work Remotely?", false, "", "", "true"));

        controls.add(new FormControl("select", "degree", "Degree", false, "", "", "Bachelor's", "Master's", "PhD"));

        controls.add(new FormControl("textarea", "summary", "Summary", false, "", ""));

        return controls;
    }
}