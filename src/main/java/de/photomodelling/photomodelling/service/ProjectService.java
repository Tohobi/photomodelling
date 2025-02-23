package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Note;
import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import de.photomodelling.photomodelling.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final PhotoRepository photoRepository;

    @Autowired
    private final NoteRepository noteRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, PhotoRepository photoRepository, NoteRepository noteRepository) {
        this.projectRepository = projectRepository;
        this.photoRepository = photoRepository;
        this.noteRepository = noteRepository;
    }

    // Ein einzelnes Projekt abrufen
    public List<Project> findProjectById(Long idFilter) {
        List<Project> projectList = new ArrayList<>();
        if (idFilter == null) {
            projectRepository.findAll().forEach(projectList::add);
        } else {
            projectRepository.findById(idFilter).ifPresent(projectList::add);
        }
        return projectList;
    }

    // Ein neues Projekt speichern
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Einem Projekt mehrere Fotos hinzufügen
    public Project addPhotosToProject(Long projectId, List<Photo> photos) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            for (Photo photo : photos) {
                photo.setProject(project);
                photoRepository.save(photo); // Jedes Foto mit dem Projekt verknüpfen und speichern
            }

            return project;
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }

    // Einem Projekt mehrere Notizen hinzufügen
    public Project addNotesToProject(Long projectId, List<Note> notes) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();

            for (Note note : notes) {
                note.setProject(project);
                noteRepository.save(note); // Jede Notiz mit dem Projekt verknüpfen und speichern
            }

            return project;
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }
}
