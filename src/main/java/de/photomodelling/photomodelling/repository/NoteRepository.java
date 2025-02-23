package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Note;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoteRepository extends CrudRepository<Note, Long> {
    List<Note> findByProjectId(Long projectId);
}
