package com.minh.musicApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minh.musicApi.Models.Entity.RoomUser;
import com.minh.musicApi.Models.Entity.TempRoom;

import java.util.List;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {
    List<RoomUser> findByRoom(TempRoom room);
    void deleteByRoom(TempRoom room);
}