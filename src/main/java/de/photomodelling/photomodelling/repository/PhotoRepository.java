package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Photo;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<Photo, Long> {
}
