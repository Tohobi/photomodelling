package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Note;
import de.photomodelling.photomodelling.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> findNoteById(Long idFilter) {
        List<Note> noteList = new ArrayList<>();
        if (idFilter == null) {
            noteRepository.findAll().forEach(noteList::add);
        } else {
            noteRepository.findById(idFilter).ifPresent(noteList::add);
        }
        return noteList;
    }
}
