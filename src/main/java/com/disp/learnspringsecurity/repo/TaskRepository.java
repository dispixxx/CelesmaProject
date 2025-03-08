package com.disp.learnspringsecurity.repo;


import com.disp.learnspringsecurity.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> getTasksByProjectId(Long projectId);
    List<Task> findByAssigneeId(Long assigneeId);
}