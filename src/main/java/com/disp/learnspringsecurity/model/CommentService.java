package com.disp.learnspringsecurity.model;

import com.disp.learnspringsecurity.repo.CommentRepository;
import com.disp.learnspringsecurity.repo.TaskRepository;
import com.disp.learnspringsecurity.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<Comment> getCommentsByTaskId(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    public Comment addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        TaskHistory history = new TaskHistory();
        Task task = comment.getTask();
        history.setTask(task);
        history.setUser(comment.getAuthor());
        history.setTimestamp(comment.getCreatedAt());
        history.setDescription("Добавлен комментарий: " + comment.getText());
        task.getHistory().add(history);
        taskRepository.save(task);
        return commentRepository.save(comment);
    }
}
