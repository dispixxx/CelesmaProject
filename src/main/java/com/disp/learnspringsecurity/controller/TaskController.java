package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/create")
    public String showCreateTaskForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        return "task_create";
    }

    @PostMapping("/create")
    public String createTask(@PathVariable Long projectId,
                             @RequestParam String title,
                             @RequestParam String description,
                             @AuthenticationPrincipal UserDetails userDetails) {
        if(userRepository.findByUsername(userDetails.getUsername()).isPresent()){
            User creator = userRepository.findByUsername(userDetails.getUsername()).get();
            Project project = projectService.getProjectById(projectId);
            taskService.createTask(title, description, project, creator);
            return "redirect:/projects/" + projectId;
        }
        else{
            throw new UsernameNotFoundException(userDetails.getUsername());
        }
    }
}