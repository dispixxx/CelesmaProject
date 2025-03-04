package com.disp.learnspringsecurity.model;


import com.disp.learnspringsecurity.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(String title, String description, Project project, User creator) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);
        task.setCreator(creator);
        return taskRepository.save(task);
    }

    public List<Task> getTasksByProject(Project project) {
        return taskRepository.findByProjectId(project.getId());
    }
}
