package com.minh.musicApi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Dto.SongTitleArtistDto;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Repository.SongRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    private static final String UPLOAD_DIR = "../uploads/songs/";

    /**
     * Upload bài hát từ MultipartFile (thường dùng cho upload qua API)
     */
    public void uploadSong(MultipartFile file, String title, String artist) throws IOException {
        // Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Tạo tên file duy nhất để tránh trùng
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file ra ổ đĩa
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo entity Song
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setFilePath(filePath.toString());
        song.setOriginalFileName(file.getOriginalFilename());
        song.setFileSize(file.getSize());
        song.setContentType(file.getContentType());

        // Lưu vào DB
        songRepository.save(song);
    }

    /**
     * Upload bài hát từ File có sẵn (trường hợp import từ server nội bộ)
     */
    public void uploadSong(File file, String title, String artist) throws IOException {
        // Tạo thư mục nếu chưa có
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Tạo tên file duy nhất
        String fileName = System.currentTimeMillis() + "_" + file.getName();
        Path targetPath = Paths.get(UPLOAD_DIR, fileName);

        // Sao chép file
        Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Tạo entity Song
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setFilePath(targetPath.toString());
        song.setOriginalFileName(file.getName());
        song.setFileSize(Files.size(file.toPath()));
        song.setContentType(Files.probeContentType(file.toPath()));

        // Lưu vào DB
        songRepository.save(song);
    }

    public ResponseEntity<Resource> playSong(Long songId) throws Exception {
        // Lấy thông tin bài hát từ DB
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        Path path = Paths.get(song.getFilePath());
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found on server");
        }

        Resource resource = new UrlResource(path.toUri());

        // Xác định content type
        String contentType = song.getContentType() != null ? song.getContentType() : "audio/mpeg";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + song.getOriginalFileName() + "\"")
                .body(resource);
    }

    public List<SongProjection> getAllSongs() {
        return songRepository.findAllProjected();
    }

    public List<SongTitleArtistDto> findByName(String name) {
        return songRepository.findByTitleContainingIgnoreCase(name);
    }

    public Song getSongById(Long songId) {
        return songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));
    }
}