package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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

    // Alle Projekte eines bestimmten Benutzers abrufen
    @GetMapping(path = "/byUser/{userId}")
    public List<Project> getProjectsByUserId(@PathVariable("userId") Long userId) {
        return projectService.findProjectsByUserId(userId);
    }

    // Alle Projekte außer eigene Projekte abrufen
    @GetMapping(path = "/others/{userId}")
    public List<Project> getProjectsFromOtherUsers(@PathVariable("userId") Long userId) {
        return projectService.findProjectsFromOtherUsers(userId);
    }

    // Neues Projekt erstellen
    @PostMapping("/create")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }
}
