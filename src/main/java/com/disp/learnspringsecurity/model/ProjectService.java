package com.disp.learnspringsecurity.model;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.repo.ProjectMemberRepository;
import com.disp.learnspringsecurity.repo.UserRepository;
import com.disp.learnspringsecurity.repo.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    // Сохранение проекта с установкой роли создателя как "ADMIN"
    public void saveProject(Project project) {
        String username = authenticationFacade.getAuthenticatedUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        project.setOwnerUser(user); // Устанавливаем владельца проекта
        if (project.getMembers() == null) {
            project.setMembers(new HashSet<ProjectMember>());
        }
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRole(ProjectRole.ADMIN); // Устанавливаем роль "ADMIN" для создателя
        project.getMembers().add(member);
        projectRepository.save(project);
    }

    // Добавление участника в проект с указанием роли
    public void addMemberToProject(Long projectId, Long userId, ProjectRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        project.addMember(user, role); // Добавляем участника с ролью
        projectRepository.save(project);
    }

    // Обновление роли участника в проекте
    public void updateMemberRole(Long projectId, Long userId, ProjectRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        project.updateMemberRole(user, role); // Обновляем роль участника
        projectRepository.save(project);
    }

    // Получение проектов, в которых участвует пользователь
    public List<Project> getProjectsByMember(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Находим ProjectMember по пользователю
        List<ProjectMember> projectMembers = projectMemberRepository.findByUser(user);

        // Собираем уникальные проекты из ProjectMember
        return projectMembers.stream()
                .map(ProjectMember::getProject)
                .distinct()
                .collect(Collectors.toList());
    }

    // Получение проекта по ID с участниками
    public Project getProjectById(Long id) {
        return projectRepository.findByIdWithMembers(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    // Получение всех проектов
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Поиск проектов по названию или ID
    public List<Project> searchProjects(String query) {
        if (isNumeric(query)) {
            Long id = Long.parseLong(query);
            Optional<Project> project = projectRepository.findById(id);
            return project.map(List::of).orElse(List.of());
        } else {
            return projectRepository.findByNameContaining(query);
        }
    }

    // Проверка, является ли строка числом
    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<ProjectMember> getSortedProjectMembers(Long projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);

        Map<ProjectRole, Integer> rolePriority = new HashMap<>();
        rolePriority.put(ProjectRole.ADMIN, 1); // Высший приоритет
        rolePriority.put(ProjectRole.MODERATOR, 2); // Средний приоритет
        rolePriority.put(ProjectRole.MEMBER, 3); // Низший приоритет

        // Сортируем участников по приоритету роли
        members.sort(Comparator.comparing(member -> rolePriority.getOrDefault(member.getRole(), Integer.MAX_VALUE)));

        return members;
    }
}

