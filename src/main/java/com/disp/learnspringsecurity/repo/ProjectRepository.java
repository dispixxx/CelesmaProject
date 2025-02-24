package com.disp.learnspringsecurity.repo;

import com.disp.learnspringsecurity.model.MyUser;
import com.disp.learnspringsecurity.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByParticipantsContaining(MyUser user);
}
