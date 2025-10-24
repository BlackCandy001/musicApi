package com.minh.musicApi.Models.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "Songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String artist;

    // THAY ĐỔI: Lưu đường dẫn file thay vì byte[]
    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    private String filePath;

    // Thêm field lưu tên file gốc (optional)
    @Column(columnDefinition = "NVARCHAR(255)")
    private String originalFileName;

    // Thêm field lưu kích thước file (optional - để hiển thị)
    @Column
    private Long fileSize;

    // Thêm field lưu loại file (optional)
    @Column(columnDefinition = "NVARCHAR(50)")
    private String contentType;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToMany(mappedBy = "songs", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private List<Playlist> playlists;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}