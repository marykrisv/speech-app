package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.Author;
import com.speech.spechapp.speech.dto.Speech;
import com.speech.spechapp.speech.dto.request.CreateSpeechRequest;
import com.speech.spechapp.speech.dto.request.ShareSpeechRequest;
import com.speech.spechapp.speech.dto.request.UpdateAuthorRequest;
import com.speech.spechapp.speech.dto.request.UpdateSpeechRequest;
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

    public List<Speech> getAllSpeeches(
            String searchBy, String query, LocalDate from, LocalDate to
    ) {
        List<Speech> speeches = null;

        if (searchBy != null) {
            switch (searchBy) {
                case "text":
                    speeches = speechRepository.searchByText(query);
                    break;
                case "key_words":
                    speeches = speechRepository.searchByKeyWords(query);
                    break;
                case "date":
                    speeches = speechRepository.findAllByDateBetween(from, to);
                    break;
            }
        } else {
            speeches = speechRepository.findAll();
        }

        return speeches;
    }

    public void addSpeech(CreateSpeechRequest createSpeechRequest) {
        Author author = createSpeechRequest.getAuthor();

        authorRepository.save(author);

        speechRepository.save(Speech.builder()
                .text(createSpeechRequest.getText())
                .subject(createSpeechRequest.getSubject())
                .author(author)
                .date(createSpeechRequest.getDate())
                .build());
    }

    public Speech getSpeech(Long id) {
        return speechRepository.findById(id).get();
    }

    public Author getSpeechAuthor(Long id) {
        return speechRepository.findById(id).get().getAuthor();
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
            responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .body(String.format("Speeches (%s) was sent to %s",
                            getSpeechesSubject(speeches),
                            shareSpeechRequest.getEmail()));
        }

        return responseEntity;
    }

    private String getSpeechesSubject(List<Speech> speeches) {
        return speeches.stream().map(Speech::getSubject).collect(Collectors.joining(", "));
    }
}
