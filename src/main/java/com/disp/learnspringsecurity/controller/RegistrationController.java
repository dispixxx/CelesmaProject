package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.MyUser;
import com.disp.learnspringsecurity.model.MyUserDetailsService;
import com.disp.learnspringsecurity.repo.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

//@RestController
//public class RegistrationController {
//
//    @Autowired
//    private MyUserRepository myUserRepository;
//
//    @Autowired
//    private MyUserDetailsService myUserDetailsService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @PostMapping("/register/user")
//    public String createUser(@RequestBody MyUser user) {
//        Optional<MyUser> existingUser = myUserRepository.findByUsername(user.getUsername());
//        if (existingUser.isPresent()) {
//            return "Error: Username already taken!";
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole("USER");
//        myUserRepository.save(user);
//        return "User registered successfully!";
//    }
//
//}

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private MyUserRepository myUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user")
    public String createUser(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             RedirectAttributes redirectAttributes) {
        // Check if username already exists
        if (myUserRepository.findByUsername(username).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username already taken!");
            return "redirect:/register";
        }

        MyUser user = new MyUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        myUserRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login";
    }
}

