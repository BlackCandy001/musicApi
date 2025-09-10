package com.minh.musicApi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Entity.Playlist;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Repository.PlaylistRepository;
import com.minh.musicApi.Repository.SongRepository;
import com.minh.musicApi.Repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SongRepository songRepository;

    // Lấy danh sách playlist của người dùng
    public List<Playlist> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    // Tạo playlist mới
    public Playlist createPlaylist(Long userId, String playlistName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Playlist playlist = new Playlist();
        playlist.setName(playlistName);
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    public Playlist addSongToPlaylist(Long userId, Long playlistId, Long songId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("User not found"));
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        Playlist playlist = optionalPlaylist
                .orElseThrow(() -> new NoSuchElementException("Danh sách phát không tồn tại."));

        if (!playlist.getUser().getId().equals(userId)) {
            throw new Exception("Người dùng không có quyền trên danh sách phát này.");
        }
        Optional<Song> optionalSong = songRepository.findById(songId);
        Song song = optionalSong.orElseThrow(() -> new NoSuchElementException("Bài hát không tồn tại."));

        playlist.getSongs().add(song);
        return playlistRepository.save(playlist);
    }

     public List<SongProjection> getSongsByPlaylist(Long playlistId) {
        return songRepository.findAllSongsByPlaylistId(playlistId);
    }
    public boolean deletePlaylist(Long userId, Long playlistId) {
        // Kiểm tra xem playlist có tồn tại không
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, userId);
        if (playlist != null) {
            // Xóa playlist
            playlistRepository.delete(playlist);
            return true;
        }
        return false; // Nếu playlist không tồn tại, trả về false
    }
}