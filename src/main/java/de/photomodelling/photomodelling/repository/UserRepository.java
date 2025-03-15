package de.photomodelling.photomodelling.repository;

import de.photomodelling.photomodelling.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
