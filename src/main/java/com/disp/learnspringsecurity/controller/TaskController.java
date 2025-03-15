package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.util.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
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

        List<Task> projectTasks = taskService.getTasksByProjectId(projectId);
        Project project = projectService.getProjectById(projectId);
        List<Task> createdTasksInProject =taskService.getTasksByCreatorIdAndProject(currentUser.getId(), projectId); // Задачи, где пользователь создатель
        List<Task> assignedTasksInProject =taskService.getTasksByAssigneeIdAndProjectId(currentUser.getId(), projectId); // Задачи, где пользователь исполнитель

        model.addAttribute("createdTasks", createdTasksInProject);
        model.addAttribute("assignedTasks", assignedTasksInProject);
        model.addAttribute("allTasks", projectTasks);
        model.addAttribute("projectName", project.getName());
        model.addAttribute("projectId", project.getId());

        return "project_tasks";
    }

    @GetMapping("/{id}")
    public String getTaskDetails(@PathVariable Long id, @PathVariable Long projectId, Model model) {
        Task task = taskService.getTaskById(id);
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
        List<User> projectUsers = project.getMembers().stream()
                .map(ProjectMember::getUser)
                .toList();
        boolean isMember = projectUsers.contains(currentUser);
        boolean isAdminOrModerator = false;
        if (isMember) {
            ProjectRole role = project.getMemberRole(currentUser);
            isAdminOrModerator = role == ProjectRole.ADMIN || role == ProjectRole.MODERATOR;
        }
        User creator = task.getCreator();
        User assignee = task.getAssignee();
        boolean isCreatorOrAssignee = false;
        if(currentUser.equals(creator) || currentUser.equals(assignee)){
            isCreatorOrAssignee = true;
        };

        model.addAttribute("task", task);
        model.addAttribute("taskStatus",TaskStatus.values());
        model.addAttribute("projectId",projectId);
        model.addAttribute("isAdminOrModerator", isAdminOrModerator);
        model.addAttribute("isCreatorOrAssignee", isCreatorOrAssignee);
        return "task";
    }

    @PostMapping("/{id}/status")
    public String changeTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status, @PathVariable String projectId) {
        taskService.changeTaskStatus(id, status);
        return "redirect:/projects/" + projectId + "/tasks/" + id;
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