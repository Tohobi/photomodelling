package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Rating;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    List<Rating> findByProjectId(Long projectId);
}
