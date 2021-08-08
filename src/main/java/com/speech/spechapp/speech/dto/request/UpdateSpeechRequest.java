package com.speech.spechapp.speech.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateSpeechRequest {
    private String text;
    private String subject;
    private LocalDate date;
}
