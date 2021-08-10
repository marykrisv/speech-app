package com.speech.spechapp.speech;

import com.speech.spechapp.email.EmailService;
import com.speech.spechapp.speech.dto.Author;
import com.speech.spechapp.speech.dto.Speech;
import com.speech.spechapp.speech.dto.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeechService {

    private final SpeechRepository speechRepository;
    private final AuthorRepository authorRepository;
    private final EmailService emailService;

    public ResponseEntity getAllSpeeches(
            String searchBy, String query, LocalDate from, LocalDate to, Long authorId
    ) {
        List<Speech> speeches = null;
        ResponseEntity responseEntity;

        if (searchBy != null) {
            switch (searchBy) {
                case "text":
                    speeches = speechRepository.searchByText(query);
                    break;
                case "subject":
                    speeches = speechRepository.searchByKeyWords(query);
                    break;
                case "date":
                    speeches = speechRepository.findAllByDateBetween(from, to);
                    break;
                case "author":
                    speeches = speechRepository.findAllByAuthor(getAuthor(authorId));
                    break;
            }
        } else {
            speeches = speechRepository.findAll();
        }

        if (speeches.isEmpty()) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Empty List!");
        } else {
            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(speeches);
        }
        return responseEntity;
    }

    private Author getAuthor(Long id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isPresent()) {
            return optionalAuthor.get();
        } else {
            return null;
        }
    }

    public void addSpeech(CreateSpeechRequest createSpeechRequest) {
        CreateAuthorRequest createAuthorRequest = createSpeechRequest.getCreateAuthorRequest();

        Author author = Author.builder()
                .firstName(createAuthorRequest.getFirstName())
                .lastName(createAuthorRequest.getLastName())
                .email(createAuthorRequest.getEmail()).build();

        authorRepository.save(author);

        speechRepository.save(Speech.builder()
                .text(createSpeechRequest.getText())
                .subject(createSpeechRequest.getSubject())
                .author(author)
                .date(createSpeechRequest.getDate())
                .build());
    }

    public ResponseEntity getSpeech(Long id) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);
        ResponseEntity responseEntity;

        if (optionalSpeech.isPresent()) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(optionalSpeech.get());
        } else {
            responseEntity = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No speech found!");
        }

        return responseEntity;
    }

    public ResponseEntity getSpeechAuthor(Long id) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);
        ResponseEntity responseEntity;

        if (optionalSpeech.isPresent()) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(optionalSpeech.get().getAuthor());
        } else {
            responseEntity = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No speech found!");
        }

        return responseEntity;
    }

    public HttpStatus updateSpeech(Long id, UpdateSpeechRequest updateSpeechRequest) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);

        if (optionalSpeech.isPresent()) {
            Speech speech = optionalSpeech.get();

            speech.setText(updateSpeechRequest.getText());
            speech.setSubject(updateSpeechRequest.getSubject());
            speech.setDate(updateSpeechRequest.getDate());

            speechRepository.save(speech);

            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_MODIFIED;
        }
    }

    public HttpStatus updateSpeechAuthor(Long id, UpdateAuthorRequest updateAuthorRequest) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);

        if (optionalSpeech.isPresent()) {
            Author author = optionalSpeech.get().getAuthor();

            author.setFirstName(updateAuthorRequest.getFirstName());
            author.setLastName(updateAuthorRequest.getLastName());
            author.setEmail(updateAuthorRequest.getEmail());

            authorRepository.save(author);

            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_MODIFIED;
        }
    }

    public HttpStatus deleteSpeech(Long id) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);

        if (optionalSpeech.isPresent()) {
            speechRepository.delete(optionalSpeech.get());

            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_MODIFIED;
        }
    }

    public ResponseEntity shareSpeech(ShareSpeechRequest shareSpeechRequest) {
        List<Speech> speeches = (List<Speech>) speechRepository.findAllById(shareSpeechRequest.getSpeechIds());

        ResponseEntity responseEntity;

        if (speeches.isEmpty()) {
            responseEntity = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(String.format("Speeches not found and was not sent to %s", shareSpeechRequest.getEmail()));
        } else {
            emailService.sendEmail(shareSpeechRequest.getEmail(), createEmailBody(speeches));

            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(String.format("Speeches (%s) was sent to %s",
                            getSpeechesSubject(speeches),
                            shareSpeechRequest.getEmail()));
        }

        return responseEntity;
    }

    private String createEmailBody(List<Speech> speeches) {
        String emailBody = "";

        for (Speech speech : speeches) {
            emailBody = emailBody.concat(String.format("Subject: %s \n", speech.getSubject()));
            emailBody = emailBody.concat(String.format("Author: %s %s \n", speech.getAuthor().getFirstName(), speech.getAuthor().getLastName()));
            emailBody = emailBody.concat(String.format("Speech Date: %s \n", speech.getDate()));
            emailBody = emailBody.concat(String.format("Speech Text: %s \n\n", speech.getText()));
        }

        return emailBody;
    }

    private String getSpeechesSubject(List<Speech> speeches) {
        return speeches.stream().map(Speech::getSubject).collect(Collectors.joining(", "));
    }
}
