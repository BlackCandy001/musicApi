package com.minh.musicApi.Controllers;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping
    public List<SongProjection> getAllSongs() {
        return songService.getAllSongs();
    }

    @PostMapping
    public String uploadSong(@RequestParam MultipartFile file, @RequestParam String title, @RequestParam String artist)
            throws IOException {
        songService.uploadSong(file, title, artist);
        return "Tải Bài Hát Thành Công!";
    }

    @GetMapping("/{songId}/play")
    public ResponseEntity<byte[]> playSong(@PathVariable Long songId) {
        byte[] audioData = songService.playSong(songId);
        if (audioData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mp3")
                .body(audioData);
    }
    @GetMapping("/search")
    public List<Song> findListSong(@RequestParam String title) {
        return songService.findByName(title);
    }
}
