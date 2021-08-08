package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.Author;
import com.speech.spechapp.speech.dto.Speech;
import com.speech.spechapp.speech.dto.request.CreateSpeechRequest;
import com.speech.spechapp.speech.dto.request.UpdateAuthorRequest;
import com.speech.spechapp.speech.dto.request.UpdateSpeechRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            return HttpStatus.NO_CONTENT;
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
            return HttpStatus.NO_CONTENT;
        }
    }

    public HttpStatus deleteSpeech(Long id) {
        Optional<Speech> optionalSpeech = speechRepository.findById(id);

        if (optionalSpeech.isPresent()) {
            speechRepository.delete(optionalSpeech.get());

            return HttpStatus.OK;
        } else {
            return HttpStatus.NO_CONTENT;
        }
    }
}
