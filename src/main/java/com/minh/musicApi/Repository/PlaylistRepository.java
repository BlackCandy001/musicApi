package com.minh.musicApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minh.musicApi.Models.Entity.Playlist;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserId(Long userId);
    Playlist findByIdAndUserId(Long playlistId, Long userId);
}