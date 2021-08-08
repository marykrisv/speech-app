package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.Speech;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface SpeechRepository extends CrudRepository<Speech, Long> {

    @Override
    List<Speech> findAll();

    @Query(value = "SELECT * FROM speeches sp " +
            "WHERE sp.text LIKE %?1%"
            , nativeQuery = true)
    List<Speech> searchByText(String query);

    @Query(value = "SELECT * FROM speeches sp " +
            "WHERE sp.key_words LIKE %?1%"
            , nativeQuery = true)
    List<Speech> searchByKeyWords(String query);

    List<Speech> findAllByDateBetween(LocalDate from, LocalDate to);
}
