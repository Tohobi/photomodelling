package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.User;
import de.photomodelling.photomodelling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Benutzer nach ID abrufen
    public List<User> findUserById(Long idFilter) {
        List<User> userList = new ArrayList<>();
        if (idFilter == null) {
            userRepository.findAll().forEach(userList::add);
        } else {
            userRepository.findById(idFilter).ifPresent(userList::add);
        }
        return userList;
    }

    // Neuen Benutzer speichern
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Passwort hashen
        return userRepository.save(user);
    }

    // Benutzer nach Benutzernamen suchen
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
