package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.Author;
import com.speech.spechapp.speech.dto.Speech;
import com.speech.spechapp.speech.dto.request.CreateSpeechRequest;
import com.speech.spechapp.speech.dto.request.UpdateAuthorRequest;
import com.speech.spechapp.speech.dto.request.UpdateSpeechRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/speeches")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    @GetMapping
    public List<Speech> getAllSpeeches(
            @RequestParam(name = "searchBy", required = false) String searchBy,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "from", required = false) LocalDateTime from,
            @RequestParam(name = "to", required = false) LocalDateTime to
    ) {
        return speechService.getAllSpeeches(searchBy, query, from, to);
    }

    @GetMapping("/{id}/author")
    public Author getSpeechAuthor(@PathVariable Long id) {
        return speechService.getSpeechAuthor(id);
    }

    @GetMapping("/{id}")
    public Speech getSpeech(@PathVariable Long id) {
        return speechService.getSpeech(id);
    }

    @PostMapping
    public ResponseEntity addSpeech(@RequestBody CreateSpeechRequest createSpeechRequest) {
        speechService.addSpeech(createSpeechRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/author")
    public ResponseEntity updateSpeechAuthor(
            @PathVariable Long id,
            @RequestBody UpdateAuthorRequest updateAuthorRequest
    ) {
        return ResponseEntity.status(speechService.updateSpeechAuthor(id, updateAuthorRequest)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateSpeech(
            @PathVariable Long id,
            @RequestBody UpdateSpeechRequest updateSpeechRequest
    ) {
        return ResponseEntity.status(speechService.updateSpeech(id, updateSpeechRequest)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSpeech(@PathVariable Long id) {
        return ResponseEntity.status(speechService.deleteSpeech(id)).build();
    }
}
