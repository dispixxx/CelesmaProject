package com.disp.learnspringsecurity.repo;

import com.disp.learnspringsecurity.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);
//    Optional<MyUser> findByEmail(String username);
}
