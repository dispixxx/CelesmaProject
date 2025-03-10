package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.util.AuthenticationFacade;
import com.disp.learnspringsecurity.model.*;
import com.disp.learnspringsecurity.repo.ProjectMemberRepository;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;


    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project_create";
    }

    @GetMapping("/{projectId}")
    public String viewProject(@PathVariable Long projectId, Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
        List<ProjectMember> members = projectService.getSortedProjectMembers(project.getId());
        List<User> projectUsers = project.getMembers().stream()
                .map(ProjectMember::getUser)
                .toList();

        // Проверяем, является ли пользователь участником проекта
        boolean isMember = projectUsers.contains(currentUser);
        // Проверяем, является ли пользователь администратором или модератором
        boolean isAdminOrModerator = false;
        if (isMember) {
            ProjectRole role = project.getMemberRole(currentUser);
            isAdminOrModerator = role == ProjectRole.ADMIN || role == ProjectRole.MODERATOR;
        }
        model.addAttribute("project", project);
        model.addAttribute("members", members);
        model.addAttribute("projectUsers", projectUsers);
        model.addAttribute("currentUser", currentUser); // Передаем текущего пользователя
        model.addAttribute("projectRoles", ProjectRole.values());
        model.addAttribute("isMember", isMember);
        model.addAttribute("isAdminOrModerator", isAdminOrModerator);
        return "project_view";
    }

    @PostMapping("/{projectId}/join")
    public String joinProject(@PathVariable Long projectId) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
        projectService.addJoinRequest(project, currentUser);
        return "redirect:/projects/" + projectId;
    }

/*    @PostMapping("/{projectId}/approve/{userId}")
    public String approveJoinRequest(@PathVariable Long projectId, @PathVariable Long userId) {
        Project project = projectService.getProjectById(projectId);
        User user = userDetailsService.getUserById(userId);
        projectService.approveJoinRequest(project, user);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/reject/{userId}")
    public String rejectJoinRequest(@PathVariable Long projectId, @PathVariable Long userId) {
        Project project = projectService.getProjectById(projectId);
        User user = userDetailsService.getUserById(userId);
        projectService.rejectJoinRequest(project, user);
        return "redirect:/projects/" + projectId;
    }*/

    @GetMapping("/search") // Поиск проектов
    public String projectSearch(@RequestParam(name = "query", required = false) String query, Model model) {
        List<Project> projects;
        boolean searchPerformed = false; // Флаг, указывающий, был ли выполнен поиск

        if (query != null && !query.isEmpty()) {
            // Ищем проекты по названию или ID
            projects = projectService.searchProjects(query);
            searchPerformed = true; // Поиск выполнен
        } else {
            // Если запрос пустой, не показываем проекты
            projects = List.of(); // Пустой список
        }

        model.addAttribute("projects", projects); // Передаем список проектов на страницу
        model.addAttribute("query", query); // Передаем поисковый запрос для отображения в поле ввода
        model.addAttribute("searchPerformed", searchPerformed); // Передаем флаг выполнения поиска
        return "search";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") Project project){
        projectService.saveProject(project);
        return "redirect:/dashboard";
    }

}
