package com.minh.musicApi.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Dto.SongTitleArtistDto;
import com.minh.musicApi.Models.Entity.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
     // Ghi đè phương thức findAll để sử dụng projection
     @Query("SELECT s.id AS id, s.title AS title, s.artist AS artist FROM Song s")
     List<SongProjection> findAllProjected();
 
     // Truy vấn các bài hát trong một playlist, chỉ lấy các trường cần thiết
     @Query("SELECT s.id AS id, s.title AS title, s.artist AS artist " +
            "FROM Song s JOIN s.playlists p WHERE p.id = :playlistId")
     List<SongProjection> findAllSongsByPlaylistId(@Param("playlistId") Long playlistId);

    List<SongTitleArtistDto> findByTitleContainingIgnoreCase(String name);
}