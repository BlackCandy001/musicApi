package com.minh.musicApi.Models.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SongInfo {
    private Long id;
    private String title;
    private String artist;
    private LocalDateTime uploadedAt;
}
