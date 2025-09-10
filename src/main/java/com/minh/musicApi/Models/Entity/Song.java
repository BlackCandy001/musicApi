package com.minh.musicApi.Models.Entity;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

        @Column(nullable = false , columnDefinition = "NVARCHAR(255)")
        private String title;

        @Column(nullable = false , columnDefinition = "NVARCHAR(255)")
        private String artist;

        @Lob
        @Column(nullable = false)
        @JsonSerialize(using = ByteArrayToBase64Serializer.class) 
        private byte[] fileData;

        @Column(nullable = false)
        private LocalDateTime uploadedAt;

        @ManyToMany(mappedBy = "songs", fetch = FetchType.LAZY)
        @JsonBackReference  
        private List<Playlist> playlists;

        @PrePersist
        protected void onCreate() {
            uploadedAt = LocalDateTime.now();
        }
    }