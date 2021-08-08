package com.speech.spechapp.speech.dto.request;

import com.speech.spechapp.speech.dto.Author;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSpeechRequest {
    private String text;
    private String subject;
    private Author author;
    private LocalDate date;
}
