package com.minh.musicApi.Models.Dto;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
public class SongListDTO {
    private Long id;
    private String title;
    private String artist;
}