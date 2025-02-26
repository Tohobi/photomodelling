package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.*;
import de.photomodelling.photomodelling.observer.ProjectNotifier;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import de.photomodelling.photomodelling.repository.PhotoRepository;
import de.photomodelling.photomodelling.repository.NoteRepository;
import de.photomodelling.photomodelling.repository.ThreeDRepository;
import de.photomodelling.photomodelling.strategy.AverageRatingStrategy;
import de.photomodelling.photomodelling.strategy.RatingStrategy;
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
    private final ThreeDRepository threeDRepository;

    //@Autowired
    private RatingStrategy ratingStrategy;

    private final ProjectNotifier projectNotifier;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, PhotoRepository photoRepository, NoteRepository noteRepository, ThreeDRepository threeDRepository, ProjectNotifier projectNotifier) {
        this.projectRepository = projectRepository;
        this.photoRepository = photoRepository;
        this.noteRepository = noteRepository;
        this.threeDRepository = threeDRepository;
        this.ratingStrategy = new AverageRatingStrategy();
        this.projectNotifier = projectNotifier;
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
        Project savedProject = projectRepository.save(project);
        projectNotifier.notifyProjectCreated(savedProject);
        return savedProject;
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

    // 3D-Modell für ein Projekt rendern
    public ThreeD render3DModelForProject(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            List<Photo> photos = photoRepository.findByProjectId(projectId);

            if (photos.isEmpty()) {
                throw new RuntimeException("Keine Fotos für das Projekt gefunden! Rendering nicht möglich.");
            }

            // Simuliertes Rendering des 3D-Modells
            ThreeD threeDModel = new ThreeD();
            threeDModel.setProject(project);
            threeDModel.setFilepath("/3dmodels/" + project.getId() + "_model.obj");
            threeDModel.setFilename(project.getName() + "_model.obj");

            return threeDRepository.save(threeDModel);
        } else {
            throw new RuntimeException("Projekt mit ID " + projectId + " nicht gefunden!");
        }
    }

    public void setRatingStrategy(RatingStrategy strategy) {
        this.ratingStrategy = strategy;
    }

    public double calculateProjectRating(Long projectId) {
        List<Rating> ratings = projectRepository.findById(projectId).map(Project::getRatings).orElse(new ArrayList<>());
        return ratingStrategy.calculateRating(ratings);
    }
}
