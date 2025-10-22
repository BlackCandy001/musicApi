package com.minh.musicApi.Controllers;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Service.SongService;
import com.minh.musicApi.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired

    @PostMapping("/home")
    public String home() {
        return "home";
    //public User register(@Validated @RequestBody User user) {
      //  return userService.registerUser(user);
    }
}
