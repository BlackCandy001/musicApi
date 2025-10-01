package com.minh.musicApi.Models.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Getter
@Setter
@Table(name = "room_user")
public class RoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết đến phòng
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonBackReference
    private TempRoom room;

    // Liên kết đến user
    @ManyToOne
    private User user;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();
}
