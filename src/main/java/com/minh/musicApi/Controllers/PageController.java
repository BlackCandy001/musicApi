package com.minh.musicApi.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "index";
    }

    @GetMapping("/log")
    public String loginPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "login";
    }

    @GetMapping("/reg")
    public String registerPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "register";
    }

    @GetMapping("/pl")
    public String playlistsPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "playlists";
    }

    @GetMapping("/up")
    public String uploadPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "upload-song";
    }

    @GetMapping("/song")
    public String songPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "songSv";
    }
    @GetMapping("/ytpl")
    public String songYoutubePlPage(HttpSession session, Model model) {
        addAuthStatusToModel(session, model);
        return "ytPlaylist";
    }

    private void addAuthStatusToModel(HttpSession session, Model model) {
        model.addAttribute("isAuthenticated", session.getAttribute("userId") != null);
    }
}