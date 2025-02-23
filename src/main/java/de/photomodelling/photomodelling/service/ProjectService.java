package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    // Alle Projekte eines bestimmten Benutzers abrufen
    public List<Project> findProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }

    // Alle Projekte außer eigene Projekte abrufen
    public List<Project> findProjectsFromOtherUsers(Long userId) {
        List<Project> allProjects = new ArrayList<>();
        projectRepository.findAll().forEach(allProjects::add);
        return allProjects.stream()
                .filter(project -> !Objects.equals(project.getUser().getId(), userId))
                .collect(Collectors.toList());
    }

    // Ein neues Projekt erstellen und speichern
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
