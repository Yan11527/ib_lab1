package com.example.program;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DataController {
    @Autowired
    private UserRepo userRepo;
    
    @GetMapping("/data")
    public String getData(Model model, HttpServletRequest request) {
        String currentUser = (String) request.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/";
        }
        
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "data";
    }

    @GetMapping("/redirect-error")
    public String redirectError() {
        return "redirect-error";
    }
}
