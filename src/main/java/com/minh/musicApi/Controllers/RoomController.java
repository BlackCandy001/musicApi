package com.minh.musicApi.Controllers;

import org.springframework.web.bind.annotation.*;

import com.minh.musicApi.Models.Entity.TempRoom;
import com.minh.musicApi.Service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Tạo phòng
    @PostMapping("/create")
    public TempRoom createRoom(@RequestParam Long ownerId, @RequestParam String roomName) {
        return roomService.createRoom(ownerId, roomName);
    }

    // Join phòng
    @PostMapping("/join")
    public TempRoom joinRoom(@RequestParam Long userId, @RequestParam String roomCode) {
        return roomService.joinRoom(userId, roomCode);
    }

    // Rời phòng
    @PostMapping("/leave")
    public void leaveRoom(@RequestParam Long userId, @RequestParam String roomCode) {
        roomService.leaveRoom(userId, roomCode);
    }
}