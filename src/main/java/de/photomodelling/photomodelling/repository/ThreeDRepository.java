package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.ThreeD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ThreeDRepository extends CrudRepository<ThreeD, Long> {
    Optional<ThreeD> findByProjectId(Long projectId);
}
