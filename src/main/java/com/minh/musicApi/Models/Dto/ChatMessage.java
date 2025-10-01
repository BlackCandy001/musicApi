package com.minh.musicApi.Models.Dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ChatMessage {
    private String roomCode;
    private String sender;
    private String content;
}