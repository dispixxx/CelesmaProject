package com.disp.learnspringsecurity.repo;

import com.disp.learnspringsecurity.model.User;
import com.disp.learnspringsecurity.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.members WHERE p.id = :id")
    Optional<Project> findByIdWithMembers(@Param("id") Long id);
    List<Project> findByMembersContaining(User user);
    //@Query("SELECT p FROM Project p WHERE p.name LIKE %:name%")
    List<Project> findByNameContaining(String name); // Поиск по названию
    Optional<Project> findById(Long id); // Поиск по ID
}
