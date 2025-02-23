package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Note;
import de.photomodelling.photomodelling.model.Photo;
import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.model.ThreeD;
import de.photomodelling.photomodelling.service.ProjectService;
import de.photomodelling.photomodelling.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PhotoService photoService;

    // Alle Projekte abrufen
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.findProjectById(null);
    }

    // Ein einzelnes Projekt nach ID abrufen
    @GetMapping(path = "/byId/{id}")
    public List<Project> getProjectById(@PathVariable("id") final Long id) {
        return projectService.findProjectById(id);
    }

    // Neues Projekt erstellen
    @PostMapping("/create")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    // Einem Projekt mehrere Fotos hinzufügen
    @PostMapping("/{projectId}/addPhotos")
    public Project addPhotosToProject(@PathVariable Long projectId, @RequestBody List<Photo> photos) {
        return projectService.addPhotosToProject(projectId, photos);
    }

    // Einem Projekt mehrere Notizen hinzufügen
    @PostMapping("/{projectId}/addNotes")
    public Project addNotesToProject(@PathVariable Long projectId, @RequestBody List<Note> notes) {
        return projectService.addNotesToProject(projectId, notes);
    }

    // 3D-Modell für ein Projekt rendern
    @PostMapping("/{projectId}/render3D")
    public ThreeD render3DModelForProject(@PathVariable Long projectId) {
        return projectService.render3DModelForProject(projectId);
    }
}
