package com.speech.spechapp.speech;

import com.speech.spechapp.speech.dto.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
