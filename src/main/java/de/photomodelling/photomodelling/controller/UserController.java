package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.model.User;
import de.photomodelling.photomodelling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Alle Benutzer abrufen
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findUserById(null);
    }

    // Einzelnen Benutzer nach ID abrufen
    @GetMapping(path = "/byId/{id}")
    public List<User> getUserById(@PathVariable("id") final Long id) {
        return userService.findUserById(id);
    }

    // Neuen Benutzer erstellen
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
