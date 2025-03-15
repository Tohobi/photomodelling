package de.photomodelling.photomodelling.controller;

import de.photomodelling.photomodelling.dto.JwtUtil;
import de.photomodelling.photomodelling.dto.LoginRequest;
import de.photomodelling.photomodelling.dto.LoginResponse;
import de.photomodelling.photomodelling.model.User;
import de.photomodelling.photomodelling.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userService.findByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(new LoginResponse(token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
