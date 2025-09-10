package com.minh.musicApi.Models.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "Playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , columnDefinition = "NVARCHAR(255)")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference 
    private User user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
      name = "playlist_song", 
      joinColumns = @JoinColumn(name = "playlist_id"), 
      inverseJoinColumns = @JoinColumn(name = "song_id"))
    @JsonManagedReference 
    private List<Song> songs;
    public Playlist() {
    }
}