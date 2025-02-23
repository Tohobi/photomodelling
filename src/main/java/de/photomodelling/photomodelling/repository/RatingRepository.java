package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.Rating;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {
}
