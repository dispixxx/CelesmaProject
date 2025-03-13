package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.util.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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

    @Autowired
    private TaskService taskService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @RequestMapping("/welcome") //Страница для НЕавторизованных пользователей
    public String handleWelcome() {
        return "index";
    }

    @GetMapping("/") //Страница для Авторизованных пользователей
    public String handleWelcome2() {
        return "index";
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
        User currentUser = userDetailsService.getUserByUsername(username);
        model.addAttribute("projects", userProjects);
        model.addAttribute("user", currentUser);
        model.addAttribute("projectRoles", ProjectRole.values());
        return "dashboard";
    }

    @GetMapping("/my-tasks")
    public String getMyTasks(Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username); // Получаем имя текущего пользователя
        List<Task> createdTasks = taskService.getTasksByCreatorId(currentUser.getId()); // Задачи, где пользователь создатель
        List<Task> assignedTasks = taskService.getTasksByAssigneeId(currentUser.getId()); // Задачи, где пользователь исполнитель

        model.addAttribute("createdTasks", createdTasks);
        model.addAttribute("assignedTasks", assignedTasks);
        return "my_tasks"; // Имя шаблона Thymeleaf
    }

    @GetMapping("/user/profile")
    public String viewUserProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        //String username = userDetails.getUsername();
        String userEmail = userDetails.getEmail();
        String userFirstName = currentUser.getFirstName();
        String userLastName = currentUser.getLastName();
        model.addAttribute("userFirstName", Objects.requireNonNullElse(userFirstName, "[FIRSTNAME]"));
        model.addAttribute("userLastName", Objects.requireNonNullElse(userLastName, "[LASTNAME]"));
        model.addAttribute("username", username);
        model.addAttribute("userEmail", userEmail);
        return "user_profile";
    }
}
