package com.disp.learnspringsecurity.repo;

import com.disp.learnspringsecurity.model.Project;
import com.disp.learnspringsecurity.model.ProjectMember;
import com.disp.learnspringsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByUser(User user);
    List<ProjectMember> findByProjectId(Long projectId);
}