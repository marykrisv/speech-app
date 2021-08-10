package com.speech.spechapp.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String email, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("marykrisvill@speech.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);
    }
}
