package com.minh.musicApi.Controllers;

import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Service.UserService;
import com.minh.musicApi.common.ResponseConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {
        
        boolean isAuthenticated = userService.authenticate(username, password);

        if (isAuthenticated) {
            User user = userService.findByUsername(username);
            if (user != null) {
                session.setAttribute("userId", user.getId());
                return ResponseEntity.ok().body(Map.of(
                    "status", ResponseConstants.Common.SUCCESS,
                    "message", ResponseConstants.Login.LOGIN_SUCCESS
                ));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", ResponseConstants.Common.ERROR,
                "field", "username",
                "message", ResponseConstants.Login.USER_NOT_FOUND
            ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "status", "error",
            "field", "password",
            "message", ResponseConstants.Login.INVALID_CREDENTIALS
        ));
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/home");
    }

    @GetMapping("/status")
    public Map<String, Object> checkAuthStatus(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        response.put("isAuthenticated", session.getAttribute("userId") != null);
        return response;
    }

    @GetMapping("/getCurrentUserId")
    public String getCurrentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId != null) {
            return "Current User ID: " + userId;
        } else {
            return "No user is logged in.";
        }
    }
}
