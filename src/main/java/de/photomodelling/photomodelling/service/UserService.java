package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.User;
import de.photomodelling.photomodelling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUserById(Long idFilter) {
        List<User> userList = new ArrayList<>();
        if (idFilter == null) {
            userRepository.findAll().forEach(userList::add);
        } else {
            userRepository.findById(idFilter).ifPresent(userList::add);
        }
        return userList;
    }
}
