package com.disp.learnspringsecurity.model;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.repo.MyUserRepository;
import com.disp.learnspringsecurity.repo.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private MyUserRepository repository;

    public Project saveProject(Project project) {
        String username = authenticationFacade.getAuthenticatedUsername();
        MyUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        project.setOwnerUser(user); // Устанавливаем пользователя
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

}

