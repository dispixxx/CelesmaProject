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

    public void createTask(String title, String description, Project project, User creator, Long assigneeId, LocalDate endDate) {
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
        taskRepository.save(task);
    }

    public List<Task> getTasksByAssigneeIdAndProjectId(Long assigneeId, Long projectId) {
        return taskRepository.findByAssigneeIdAndProjectId(assigneeId, projectId);
    }

    public List<Task> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getTasksByCreatorId(Long creatorId) {
        return taskRepository.findByCreatorId(creatorId);
    }

    public List<Task> getTasksByAssigneeId(Long creatorId) {
        return taskRepository.findByAssigneeId(creatorId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Задача не найдена"));
    }

    public void changeTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        task.setStatus(status);
        taskRepository.save(task);
    }
}
