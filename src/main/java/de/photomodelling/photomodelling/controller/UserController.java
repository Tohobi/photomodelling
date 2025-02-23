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

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findUserById(null);
    }

    @GetMapping(path = "/byId/{id}")
    public List<User> getUserById(@PathVariable("id") final Long id) {
        return userService.findUserById(id);
    }
}
