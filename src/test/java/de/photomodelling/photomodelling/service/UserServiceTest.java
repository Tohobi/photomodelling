package de.photomodelling.photomodelling.service;

import de.photomodelling.photomodelling.model.User;
import de.photomodelling.photomodelling.repository.UserRepository;
import de.photomodelling.photomodelling.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("UserA");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("UserB");
    }

    @Test
    void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findUserById(null);
        assertEquals(2, users.size());
    }

    @Test
    void testFindUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        List<User> users = userService.findUserById(1L);
        assertEquals(1, users.size());
        assertEquals("UserA", users.get(0).getUsername());
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User savedUser = userService.createUser(user1);
        assertNotNull(savedUser);
        assertEquals("UserA", savedUser.getUsername());
    }
}