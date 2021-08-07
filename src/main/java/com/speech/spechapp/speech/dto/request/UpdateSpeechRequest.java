package com.speech.spechapp.speech.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateSpeechRequest {
    private String text;
    private String keyWords;
    private LocalDateTime date;
}
