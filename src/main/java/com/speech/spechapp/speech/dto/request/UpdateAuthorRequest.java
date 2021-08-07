package com.speech.spechapp.speech.dto.request;

import lombok.Data;

@Data
public class UpdateAuthorRequest {
    private String firstName;
    private String lastName;
    private String email;
}
