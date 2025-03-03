package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


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

    @GetMapping("/admin/home") //Страница для Авторизованных(ADMIN) пользователей
    public String handleAdminHome() {
        return "home_admin";
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

    /*@GetMapping("/projects/view/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project_view";
    }*/

    @GetMapping("/projects/view/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("members", project.getMembers());
        return "project_view";
    }
}
