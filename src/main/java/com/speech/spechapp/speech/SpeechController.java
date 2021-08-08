package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.request.CreateSpeechRequest;
import com.speech.spechapp.speech.dto.request.ShareSpeechRequest;
import com.speech.spechapp.speech.dto.request.UpdateAuthorRequest;
import com.speech.spechapp.speech.dto.request.UpdateSpeechRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/speeches")
@RequiredArgsConstructor
public class SpeechController {

    private final SpeechService speechService;

    @GetMapping
    public ResponseEntity getAllSpeeches(
            @RequestParam(name = "searchBy", required = false) String searchBy,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "authorId", required = false) Long authorId
    ) {
        return speechService.getAllSpeeches(searchBy, query, from, to, authorId);
    }

    @GetMapping("/{id}/author")
    public ResponseEntity getSpeechAuthor(@PathVariable Long id) {
        return speechService.getSpeechAuthor(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getSpeech(@PathVariable Long id) {
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

    @PostMapping("/share")
    public ResponseEntity shareSpeech(@RequestBody ShareSpeechRequest shareSpeechRequest) {
        return speechService.shareSpeech(shareSpeechRequest);
    }
}
