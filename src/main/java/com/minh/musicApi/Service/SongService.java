package com.minh.musicApi.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minh.musicApi.Models.Dto.SongProjection;
import com.minh.musicApi.Models.Entity.Song;
import com.minh.musicApi.Repository.SongRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public void uploadSong(MultipartFile file, String title, String artist) throws IOException {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setFileData(file.getBytes());
        songRepository.save(song);
    }
 public void uploadSong(File file, String title, String artist) throws IOException {
        // Read the file as bytes
        byte[] fileData = Files.readAllBytes(file.toPath());

        // Create a new song entity
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setFileData(fileData);  // Set the file content as bytes
        
        // Save the song to the repository (database)
        songRepository.save(song);
    }
    public byte[] playSong(Long songId) {
        Song song = songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));
        return song.getFileData();
    }

     public List<SongProjection> getAllSongs() {
        return songRepository.findAllProjected();
    }
    public List<Song> findByName(String name){
        return songRepository.findByTitleContainingIgnoreCase(name);
    }
    public Song getSongById(Long songId) {
        return songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found"));
    }
}