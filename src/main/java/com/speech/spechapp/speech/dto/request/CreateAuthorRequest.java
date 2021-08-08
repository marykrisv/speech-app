package com.speech.spechapp.speech.dto.request;

import lombok.Data;

@Data
public class CreateAuthorRequest {
    private String firstName;
    private String lastName;
    private String email;
}
