package com.minh.musicApi.Controllers;

import com.minh.musicApi.Service.PlaylistService;
import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Entity.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public List<Playlist> getPlaylists(@PathVariable Long userId) {
        return playlistService.getPlaylistsByUserId(userId);
    }

    @PostMapping
    public Playlist createPlaylist(@PathVariable Long userId, @RequestParam String playlistName) {
        return playlistService.createPlaylist(userId, playlistName);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long userId, @PathVariable Long playlistId) {
        boolean isDeleted = playlistService.deletePlaylist(userId, playlistId);
        if (isDeleted) {
            return ResponseEntity.ok("Playlist đã được xóa.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy playlist.");
        }
    }
    @GetMapping("/{playlistId}")
    public List<SongProjection> getSongFromPlaylist(@PathVariable Long userId, @PathVariable Long playlistId) {
        List<SongProjection> songlist = playlistService.getSongsByPlaylist(playlistId);
        return songlist;
    }
    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable Long userId,
            @PathVariable Long playlistId,
            @RequestParam Long songId) throws Exception {
        Playlist updatedPlaylist = playlistService.addSongToPlaylist(userId, playlistId, songId);
        if (updatedPlaylist != null) {
            return ResponseEntity.ok("Bài hát đã được thêm vào danh sách phát.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi thêm bài hát.");
    }
}
