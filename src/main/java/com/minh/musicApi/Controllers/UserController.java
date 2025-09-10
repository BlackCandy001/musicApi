package com.minh.musicApi.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Service.UserService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@Validated @RequestBody User user) {
        return userService.registerUser(user);
    }
}
