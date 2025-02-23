package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Note;
import de.photomodelling.photomodelling.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.findNoteById(null);
    }

    @GetMapping(path = "/byId/{id}")
    public List<Note> getNoteById(@PathVariable("id") final Long id) {
        return noteService.findNoteById(id);
    }
}
