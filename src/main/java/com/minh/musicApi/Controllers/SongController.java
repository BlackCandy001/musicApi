package com.minh.musicApi.Controllers;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Dto.SongTitleArtistDto;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<Resource> playSong(@PathVariable Long songId) throws Exception {
        // Lấy thông tin bài hát từ DB
        Song song = songService.getSongById(songId); // thêm hàm getSongById() trong service
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        Path path = Paths.get(song.getFilePath());
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        // Xác định content type
        String contentType = song.getContentType() != null ? song.getContentType() : "audio/mpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + song.getOriginalFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/search")
    public List<SongTitleArtistDto> searchSongs(@RequestParam String title) {
        return songService.findByName(title);
    }
}
