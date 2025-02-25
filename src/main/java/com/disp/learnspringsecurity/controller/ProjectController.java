package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.AuthenticationFacade;
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
        return "project_form";
    }

    @GetMapping("/user/home")
    public String goHome(Model model) {
        return "redirect:/user/home";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") Project project){
        projectService.saveProject(project);
        return "redirect:/dashboard";
    }

    /*@GetMapping("/my-projects")
    public String getUserProjects(Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        List<Project> userProjects = projectService.getProjectsByParticipant(username);
        model.addAttribute("projects", userProjects);
        return "user_projects"; // Убедитесь, что у вас есть соответствующий HTML-шаблон
    }*/

    /*@GetMapping("/dashboard")
    public String getUserProjects(Model model) {
        String username = authenticationFacade.getAuthenticatedUsername();
        List<Project> userProjects = projectService.getProjectsByParticipant(username);
        model.addAttribute("projects", userProjects);
        return "dashboard"; // Убедись, что название шаблона совпадает
    }*/


}
