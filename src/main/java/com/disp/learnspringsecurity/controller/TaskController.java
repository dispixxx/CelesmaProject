package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/create")
    public String showCreateTaskForm(@PathVariable Long projectId, Model model) {
//        model.addAttribute("task", new Task());
        model.addAttribute("projectId", projectId);
        Project project = projectService.getProjectById(projectId);
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userRepository.findByUsername(username).get();
        model.addAttribute("members", project.getMembers());
        model.addAttribute("projectName", project.getName());
        model.addAttribute("creatorId", currentUser.getId());
        return "task_create";
    }

    @PostMapping("/create")
    public String createTask(@PathVariable Long projectId,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam(required = false) Long assigneeId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                             @AuthenticationPrincipal UserDetails userDetails) {
        if(userRepository.findByUsername(userDetails.getUsername()).isPresent()){
            User creator = userRepository.findByUsername(userDetails.getUsername()).get(); // Позже переделать что бы это выполнял сервис.(передавать projectID в createTask();
            Project project = projectService.getProjectById(projectId);
            taskService.createTask(title, description, project, creator, assigneeId, dueDate);
            return "redirect:/projects/" + projectId;
        }
        else{
            throw new UsernameNotFoundException(userDetails.getUsername());
        }
    }
}