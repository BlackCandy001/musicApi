package com.minh.musicApi.Models.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerAction {
    private String roomId;
    private String type; // PLAY, PAUSE, SEEK
    private int timestamp;

    // getters & setters
}

