package com.speech.spechapp.speech.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ShareSpeechRequest {
    private List<Long> speechIds;
    private String email;
}
