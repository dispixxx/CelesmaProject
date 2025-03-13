package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.repo.TaskRepository;
import com.disp.learnspringsecurity.util.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;


    @Autowired
    private AuthenticationFacade authenticationFacade;


    @Autowired
    private CustomUserDetailsService userDetailsService;



    @GetMapping
    public String getProjectsTasks(@PathVariable Long projectId, Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);

//        List<Task> userTasks = taskService.getTasksByAssigneeIdAndProjectId(currentUser.getId(),projectId);
        List<Task> allTasks = taskService.getTasksByProjectId(projectId);
        Project project = projectService.getProjectById(projectId);
        List<Task> createdTasks = taskService.getTasksByCreatorId(currentUser.getId()); // Задачи, где пользователь создатель
        List<Task> assignedTasks = taskService.getTasksByAssigneeId(currentUser.getId()); // Задачи, где пользователь исполнитель

        model.addAttribute("createdTasks", createdTasks);
        model.addAttribute("assignedTasks", assignedTasks);

//        model.addAttribute("userTasks", userTasks);
        model.addAttribute("allTasks", allTasks);
        model.addAttribute("projectName", project.getName());
        model.addAttribute("projectId", project.getId());

        return "project_tasks";
    }

    @GetMapping("/{id}")
    public String getTaskDetails(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "task";
    }

    @GetMapping("/create")
    public String showCreateTaskForm(@PathVariable Long projectId, Model model) {
        model.addAttribute("projectId", projectId);
        Project project = projectService.getProjectById(projectId);
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
//        User currentUser = userRepository.findByUsername(username).isPresent() ? userRepository.findByUsername(username).get() : null;
        model.addAttribute("members", project.getMembers());
        model.addAttribute("projectName", project.getName());
        model.addAttribute("creatorId", Objects.requireNonNull(currentUser).getId());
        return "task_create";
    }

    @PostMapping("/create")
    public String createTask(@PathVariable Long projectId,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam(required = false) Long assigneeId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetailsService.getUserByUsername(userDetails.getUsername())!=null) {
            User creator = userDetailsService.getUserByUsername(userDetails.getUsername());
            Project project = projectService.getProjectById(projectId);
            taskService.createTask(title, description, project, creator, assigneeId, endDate);
            return "redirect:/projects/" + projectId;
        }
        else{
            throw new UsernameNotFoundException(userDetails.getUsername());
        }
    }


}