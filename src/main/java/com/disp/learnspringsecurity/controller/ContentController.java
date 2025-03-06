package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
public class ContentController {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/welcome") //Страница для НЕавторизованных пользователей
    public String handleWelcome() {
        return "index";
    }

    @GetMapping("/") //Страница для Авторизованных пользователей
    public String handleWelcome2() {
        return "index";
    }

    @GetMapping("/user/home") //Страница для Авторизованных(USER) пользователей
    public String handleUserHome(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        String userEmail = userDetails.getEmail();
        model.addAttribute("username", username);
        model.addAttribute("userEmail", userEmail);
        return "home_user";
    }

    @GetMapping("/login") //Страница для ввода логина
    public String handleLogin() {
        return "custom_login";
    }

    @GetMapping("/register") //Страница для регистрации
    public String handleRegister() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String getUserProjects(Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        List<Project> userProjects = projectService.getProjectsByMember(username);
        model.addAttribute("projects", userProjects);
        return "dashboard";
    }

    @GetMapping("/user/profile")
    public String viewUserProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = authenticationFacade.getAuthenticatedUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        //String username = userDetails.getUsername();
        String userEmail = userDetails.getEmail();
        String userFirstName = userOptional.get().getFirstName();
        String userLastName = userOptional.get().getLastName();
        model.addAttribute("userFirstName", Objects.requireNonNullElse(userFirstName, "[FIRSTNAME]"));
        model.addAttribute("userLastName", Objects.requireNonNullElse(userLastName, "[LASTNAME]"));
        model.addAttribute("username", username);
        model.addAttribute("userEmail", userEmail);
        return "user_profile";
    }
}
