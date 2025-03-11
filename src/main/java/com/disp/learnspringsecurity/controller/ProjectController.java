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
    private CustomUserDetailsService userDetailsService;

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

        List<User> projectApplicants =project.getApplicants().stream().toList(); //Список тех, кто уже отправил запрос.
        boolean isApplicant = projectApplicants.contains(currentUser);

        model.addAttribute("project", project);
        model.addAttribute("members", members);
        model.addAttribute("projectUsers", projectUsers);
        model.addAttribute("currentUser", currentUser); // Передаем текущего пользователя
        model.addAttribute("projectRoles", ProjectRole.values());
        model.addAttribute("isMember", isMember);
        model.addAttribute("isAdminOrModerator", isAdminOrModerator);
        model.addAttribute("isApplicant", isApplicant);
        return "project_view";
    }

    @GetMapping("/{projectId}/manage")
    public String manageProject(@PathVariable Long projectId, Model model){
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
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

        // Проверяем, является ли пользователь администратором или модератором
        if (!isAdminOrModerator) {
            return "redirect:/projects/" + projectId; // Если нет, перенаправляем на страницу проекта
        }
        model.addAttribute("project", project);

        return "project_management";

    }

    @GetMapping("/{projectId}/manage/members")
    public String manageProjectMembers(@PathVariable Long projectId, Model model){
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
        List<ProjectMember> members = projectService.getSortedProjectMembers(project.getId());

        model.addAttribute("project", project);
        model.addAttribute("projectMembers", members);
        return "project_members";
    }

    /*@PostMapping("/{userId}/change-role")
    @ResponseBody
    public ResponseEntity<Void> changeRole(
            @PathVariable Long projectId,
            @PathVariable Long userId,
            @RequestParam String role) {
        projectService.changeMemberRole(projectId, userId, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/remove-member")
    @ResponseBody
    public ResponseEntity<Void> removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.removeMember(projectId, userId);
        return ResponseEntity.ok().build();
    }*/

    @GetMapping("/{projectId}/manage/applicants")
    public String manageProjectApplicants(@PathVariable Long projectId, Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
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

        // Проверяем, является ли пользователь администратором или модератором
        if (!isAdminOrModerator) {
            return "redirect:/projects/" + projectId; // Если нет, перенаправляем на страницу проекта
        }

        // Получаем список заявок на вступление
        List<User> applicants = project.getApplicants().stream().toList();
        model.addAttribute("applicants", applicants);
        model.addAttribute("project", project);

        return "project_applicants_requests"; // Имя шаблона для страницы управления
    }

    @PostMapping("/{projectId}/join")
    public String joinProject(@PathVariable Long projectId) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userDetailsService.getUserByUsername(username);
        Project project = projectService.getProjectById(projectId);
        projectService.addJoinRequest(project, currentUser);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/manage/applicants/approve/{userId}")
    public String approveApplicant(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.approveApplicant(projectId, userId);
        return "redirect:/projects/" + projectId + "/manage";
    }

    @PostMapping("/{projectId}/manage/applicants/reject/{userId}")
    public String rejectApplicant(@PathVariable Long projectId, @PathVariable Long userId) {
        projectService.rejectApplicant(projectId, userId);
        return "redirect:/projects/" + projectId + "/manage";
    }

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
