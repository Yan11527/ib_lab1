package com.example.program;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/auth/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) return "error";
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) return "error";
        
        return jwtUtil.generateToken(username);
    }

    @PostMapping("/auth/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (userRepo.findByUsername(username).isPresent()) {
            
            return "redirect-wait";
        }
        
        User user = new User(username, passwordEncoder.encode(password));
        userRepo.save(user);
        return "redirect-wait";
    }

    @PostMapping("/auth/delete")
    public String deleteUser(@RequestParam("username") String username) {
        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isPresent()) {
            userRepo.delete(userOpt.get());
            return "redirect:/";
        }
        return "redirect-error";
    }

}
