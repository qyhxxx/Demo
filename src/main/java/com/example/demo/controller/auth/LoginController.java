package com.example.demo.controller.auth;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        return "hello";
    }
}
