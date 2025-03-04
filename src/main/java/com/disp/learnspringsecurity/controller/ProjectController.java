package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.Project;
import com.disp.learnspringsecurity.model.ProjectService;
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

    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project_create";
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
