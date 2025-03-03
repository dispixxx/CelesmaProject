package com.disp.learnspringsecurity.controller;

import com.disp.learnspringsecurity.model.CustomUserDetailsService;
import com.disp.learnspringsecurity.model.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDto userProfileDto) {
        userService.updateUserProfile(userProfileDto);
        return ResponseEntity.ok().build();
    }
}
