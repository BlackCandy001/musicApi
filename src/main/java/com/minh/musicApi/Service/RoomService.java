package com.minh.musicApi.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.minh.musicApi.Models.Entity.RoomUser;
import com.minh.musicApi.Models.Entity.TempRoom;
import com.minh.musicApi.Models.Entity.User;
import com.minh.musicApi.Repository.RoomUserRepository;
import com.minh.musicApi.Repository.TempRoomRepository;
import com.minh.musicApi.Repository.UserRepository;

@Service
public class RoomService {

    private final TempRoomRepository tempRoomRepository;
    private final RoomUserRepository roomUserRepository;
    private final UserRepository userRepository;

    public RoomService(TempRoomRepository tempRoomRepository, RoomUserRepository roomUserRepository, UserRepository userRepository) {
        this.tempRoomRepository = tempRoomRepository;
        this.roomUserRepository = roomUserRepository;
        this.userRepository = userRepository;
    }

    // Tạo phòng mới
    public TempRoom createRoom(Long ownerId, String roomName) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TempRoom room = new TempRoom();
        room.setRoomCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        room.setRoomName(roomName);
        room.setOwner(owner);

        TempRoom savedRoom = tempRoomRepository.save(room);

        // Add owner vào phòng
        RoomUser ru = new RoomUser();
        ru.setRoom(savedRoom);
        ru.setUser(owner);
        roomUserRepository.save(ru);

        return savedRoom;
    }

    // Join phòng
    public TempRoom joinRoom(Long userId, String roomCode) {
        TempRoom room = tempRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        RoomUser ru = new RoomUser();
        ru.setRoom(room);
        ru.setUser(user);
        roomUserRepository.save(ru);

        return room;
    }

    // Rời phòng
    public void leaveRoom(Long userId, String roomCode) {
        TempRoom room = tempRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<RoomUser> participants = roomUserRepository.findByRoom(room);
        participants.removeIf(p -> p.getUser().getId().equals(userId));

        roomUserRepository.deleteAll(participants);

        // Nếu phòng trống thì xóa luôn
        if (roomUserRepository.findByRoom(room).isEmpty()) {
            roomUserRepository.deleteByRoom(room);
            tempRoomRepository.delete(room);
        }
    }
}