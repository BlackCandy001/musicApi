package com.minh.musicApi.Controllers;

import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String login(@RequestParam String username,@RequestParam String password,HttpSession session) {
        boolean isAuthenticated = userService.authenticate(username, password);
        if (isAuthenticated) {
            User user = userService.findByUsername(username);
            if (user != null) {
                session.setAttribute("userId", user.getId());
                return "Đăng nhập thành công";
            }
            return "Không tìm thấy người dùng";
        }
        return "Thông tin không hợp lệ";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/log");
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
