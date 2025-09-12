package com.minh.musicApi.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/youtube")
public class YouTubeController {

    private static final Pattern VIDEO_ID_PATTERN = Pattern.compile(
            "(?:v=|\\/)([0-9A-Za-z_-]{11})(?:[&?]|$)"
    );

    // Endpoint trả trang HTML player
    @GetMapping("/playAudio")
    public String play(@RequestParam("url") String url, Model model) {
        System.out.println("Received URL: " + url); // Debug log
        
        String videoId = extractVideoId(url);
        System.out.println("Extracted Video ID: " + videoId); // Debug log
        
        if (videoId == null) {
            model.addAttribute("error", "Không nhận diện được videoId từ URL: " + url);
            return "error";
        }
        
        // Không dùng autoplay=1 trong embed URL cho audio player
        String embedUrl = "https://www.youtube.com/embed/" + videoId + "?rel=0&modestbranding=1&enablejsapi=1";
        System.out.println("Generated Embed URL: " + embedUrl); // Debug log
        
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("videoId", videoId);
        model.addAttribute("originalUrl", url);
        
        return "youtube-player";
    }

    // Endpoint REST trả JSON
    @GetMapping("/embed")
    @ResponseBody
    public ResponseEntity<?> getEmbed(@RequestParam("url") String url) {
        String videoId = extractVideoId(url);
        if (videoId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid YouTube URL"));
        }
        String embed = "https://www.youtube.com/embed/" + videoId + "?rel=0&modestbranding=1&enablejsapi=1";
        return ResponseEntity.ok(Map.of("embedUrl", embed, "videoId", videoId));
    }

    // Endpoint để test
    @GetMapping("/test")
    public String test(Model model) {
        // URL test mặc định
        String testUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        return play(testUrl, model);
    }

    private String extractVideoId(String url) {
        if (url == null || url.trim().isEmpty()) {
            System.out.println("URL is null or empty");
            return null;
        }
        
        // Clean URL
        url = url.trim();
        System.out.println("Processing URL: " + url);
        
        // Pattern cho youtube.com/watch?v=
        Matcher m = VIDEO_ID_PATTERN.matcher(url);
        if (m.find()) {
            String videoId = m.group(1);
            System.out.println("Found video ID with main pattern: " + videoId);
            return videoId;
        }
        
        // Pattern cho youtu.be/
        Pattern shortPattern = Pattern.compile("youtu\\.be/([0-9A-Za-z_-]{11})");
        Matcher shortM = shortPattern.matcher(url);
        if (shortM.find()) {
            String videoId = shortM.group(1);
            System.out.println("Found video ID with short pattern: " + videoId);
            return videoId;
        }
        
        // Pattern cho youtube.com/embed/
        Pattern embedPattern = Pattern.compile("youtube\\.com/embed/([0-9A-Za-z_-]{11})");
        Matcher embedM = embedPattern.matcher(url);
        if (embedM.find()) {
            String videoId = embedM.group(1);
            System.out.println("Found video ID with embed pattern: " + videoId);
            return videoId;
        }
        
        System.out.println("No video ID found in URL: " + url);
        return null;
    }
}