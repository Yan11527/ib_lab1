package com.example.program;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @GetMapping("/api/data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getApiData(HttpServletRequest request) {
        String currentUser = (String) request.getAttribute("currentUser");
        if (currentUser == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Unauthorized");
            error.put("message", "token not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        List<User> users = userRepo.findAll();
        
        List<Map<String, Object>> usersData = users.stream()
            .map(user -> {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("username", user.getUsername());
                return userData;
            })
            .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("users", usersData);
        response.put("currentUser", currentUser);
        response.put("total", usersData.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/redirect-error")
    public String redirectError() {
        return "redirect-error";
    }
}
