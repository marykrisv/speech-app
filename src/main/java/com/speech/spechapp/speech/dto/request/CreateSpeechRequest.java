package com.speech.spechapp.speech.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSpeechRequest {
    private String text;
    private String subject;
    private CreateAuthorRequest createAuthorRequest;
    private LocalDate date;
}
