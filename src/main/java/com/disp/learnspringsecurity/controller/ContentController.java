package com.disp.learnspringsecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContentController {

    @RequestMapping("/welcome") //Страница для НЕавторизованных пользователей
    public String handleWelcome() {
        return "index";
    }

    @GetMapping("/") //Страница для Авторизованных пользователей
    public String handleWelcome2() {
        return "index";
    }

    @GetMapping("/home") //Страница для Авторизованных пользователей
    public String handleWelcome4AuthUsers() {
        return "dashboard";
    }

    @GetMapping("/dashboard") //Страница для Авторизованных пользователей
    public String handleWelcome4AuthUsers2() {
        return "dashboard";
    }

    @GetMapping("/admin/home") //Страница для Авторизованных(ADMIN) пользователей
    public String handleAdminHome() {
        return "home_admin";
    }

    @GetMapping("/user/home") //Страница для Авторизованных(USER) пользователей
    public String handleUserHome() {
        return "home_user";
    }

    @GetMapping("/login") //Страница для ввода логина
    public String handleLogin() {
        return "custom_login";
    }

    @GetMapping("/register") //Страница для регистрации
    public String handleRegister() {
        return "register";
    }

}
