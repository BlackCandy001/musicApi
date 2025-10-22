package com.minh.musicApi.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.minh.musicApi.Models.Entity.TempRoom;

import java.util.Optional;

public interface TempRoomRepository extends JpaRepository<TempRoom, Long> {
    Optional<TempRoom> findByRoomCode(String roomCode);
}