package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.model.Project;
import com.disp.learnspringsecurity.model.ProjectService;
import com.disp.learnspringsecurity.model.User;
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


    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project_create";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User currentUser = userRepository.findByUsername(username).get();
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("members", project.getMembers());
        model.addAttribute("user", currentUser); // Передаем текущего пользователя
        return "project_view";
    }

    /*@GetMapping("/search") //Поиск проектов
    public String projectSearch(Model model) {
        List<Project> projects = projectService.getAllProjects(); // Получаем список проектов
        model.addAttribute("projects", projects); // Передаем список проектов на страницу
        return "search";
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
