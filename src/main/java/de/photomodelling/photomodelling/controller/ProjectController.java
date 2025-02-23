package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.Project;
import de.photomodelling.photomodelling.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Project> getAllProjects() {
        List<Project> projectList = projectService.findProjectById(null);
        return projectList;
    }

    @GetMapping(path = "/byId/{id}")
    public List<Project> getProjectById(@PathVariable("id") final Long id) {
        List<Project> projectList = projectService.findProjectById(id);
        return projectList;
    }

}
