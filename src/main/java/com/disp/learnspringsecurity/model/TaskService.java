package com.disp.learnspringsecurity.model;


import com.disp.learnspringsecurity.repo.TaskRepository;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(String title, String description, Project project, User creator, Long assigneeId, LocalDate endDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setCreatedAt(LocalDateTime.now());
        task.setProject(project);
        task.setCreator(creator);
        task.setEndDate(endDate);
        if (assigneeId != null) {
            User assignee = userRepository.findById(assigneeId).orElseThrow();
            task.setAssignee(assignee);
        }
        return taskRepository.save(task);
    }

    public List<Task> getTasksByAssigneeIdAndProjectId(Long assigneeId, Long projectId) {
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
}
