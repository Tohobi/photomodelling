package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Note;
import org.springframework.data.repository.CrudRepository;

public interface NoteRepository extends CrudRepository<Note, Long> {
}
