package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Project;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectRepository extends CrudRepository<Project, Long> {
    List<Project> findByUserId(Long userId);
}
