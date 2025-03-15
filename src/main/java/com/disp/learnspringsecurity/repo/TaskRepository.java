package com.disp.learnspringsecurity.repo;


import com.disp.learnspringsecurity.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssigneeIdAndProjectId(Long assigneeId, Long projectId);
    List<Task> findByCreatorId(Long creatorId);
    List<Task> findByAssigneeId(Long assigneeId);
    List<Task> findByCreatorIdAndProjectId(Long assignee_id, Long project_id);
}