package com.speech.spechapp.speech.dto.request;

import com.speech.spechapp.speech.dto.Author;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSpeechRequest {
    private String text;
    private String keyWords;
    private Author author;
    private LocalDateTime date;
}
