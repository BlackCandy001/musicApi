package com.minh.musicApi.Controllers;

import com.minh.musicApi.Service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/music")
public class MusicUploadController {

    @Autowired
    private SongService songService;

    // @GetMapping("/uploadSongs")
    // public String uploadSongs() {
    //     String musicDirectoryPath = "E:/SrcCode/sefltProject/MusicSpringBootWeb/musicApi/src/main/java/com/minh/musicApi/Controllers/music";
    //     File musicDirectory = new File(musicDirectoryPath);

    //     if (!musicDirectory.exists() || !musicDirectory.isDirectory()) {
    //         return "Invalid music directory path.";
    //     }

    //     File[] files = musicDirectory.listFiles();
    //     if (files == null || files.length == 0) {
    //         return "No songs found in the music directory.";
    //     }

    //     for (File file : files) {
    //         if (file.isFile()) {
    //             String title = file.getName();
    //             String artist = "unknown";
    //             try {
    //                 songService.uploadSong(file, title, artist);
    //             } catch (IOException e) {
    //                 return "Error uploading song: " + title;
    //             }
    //         }
    //     }

    //     return "Songs uploaded successfully.";
    // }
    @PostMapping("/uploadSongs")
    public String uploadSong(@RequestParam MultipartFile file, @RequestParam String title, @RequestParam String artist)
            throws IOException {
        songService.uploadSong(file, title, artist);
        return "Tải Bài Hát Thành Công!";
    }
}
