package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Photo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
    List<Photo> findByProjectId(Long projectId);
}
