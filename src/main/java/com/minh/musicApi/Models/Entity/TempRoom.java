package com.minh.musicApi.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@Table(name = "temp_room")
public class TempRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // room_id

    @Column(nullable = false, unique = true)
    private String roomCode; // Mã phòng, có thể generate random (ví dụ: 6 ký tự)

    @Column(nullable = false)
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;   // Người tạo phòng

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<RoomUser> participants;
}
