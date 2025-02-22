/*
package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.Project;
import com.disp.learnspringsecurity.model.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.saveProject(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
}
*/

package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.Project;
import com.disp.learnspringsecurity.model.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

//    @GetMapping
//    public String listProjects(Model model) {
//        model.addAttribute("projects", projectService.getAllProjects());
//        return "projects";
//    }

    @GetMapping("/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "project_form";
    }

    @PostMapping("/save")
    public String saveProject(@ModelAttribute("project") Project project){
//        Long ownerId = projectService.getCurrentUserId();
//        if(ownerId != null){
//            projectService.saveProject(project, ownerId);
//        }
        projectService.saveProject(project);
        return "redirect:/projects";
    }
}
