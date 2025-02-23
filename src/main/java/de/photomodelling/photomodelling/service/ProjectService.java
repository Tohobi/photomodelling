package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findProjectById(Long idFilter) {
        List<Project> projectList = new ArrayList<>();
        if (idFilter == null) {
            projectRepository.findAll().forEach(projectList::add);
        } else {
            projectRepository.findById(idFilter).ifPresent(projectList::add);
        }
        return projectList;
    }
}
