package com.disp.learnspringsecurity.model;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.repo.MyUserRepository;
import com.disp.learnspringsecurity.repo.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private MyUserRepository userRepository;

/*    public void saveProject(Project project) {
        String username = authenticationFacade.getAuthenticatedUsername();
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        project.setOwnerUser(user); // Устанавливаем пользователя
        if(project.getParticipants() == null){
            project.setParticipants(new HashSet<>());
        }
        project.getParticipants().add(user);
        projectRepository.save(project);
    }*/

    public void saveProject(Project project) {
        String username = authenticationFacade.getAuthenticatedUsername();
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        project.setOwnerUser(user); // Устанавливаем пользователя
        if(project.getMembers() == null){
            project.setMembers(new HashSet<>());
        }
        project.getMembers().add(user);
        projectRepository.save(project);
    }

    public List<Project> getProjectsByMember(String username) {
        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return projectRepository.findByMembersContaining(user);
    }

/*    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }*/

    public Project getProjectById(Long id) {
        return projectRepository.findByIdWithMembers(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }


    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> searchProjects(String query) {
        // Проверяем, является ли запрос числом
        if (isNumeric(query)) {
            Long id = Long.parseLong(query);
            Optional<Project> project = projectRepository.findById(id);
            return project.map(List::of).orElse(List.of()); // Возвращаем список с одним проектом или пустой список
        } else {
            // Ищем по названию
            return projectRepository.findByNameContaining(query);
        }
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

