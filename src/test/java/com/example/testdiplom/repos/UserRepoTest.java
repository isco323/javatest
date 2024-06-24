package com.example.testdiplom.repos;


import com.example.testdiplom.domain.Role;
import com.example.testdiplom.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setEmail("test@example.com");
        testUser.setActive(true);
        testUser.setActivationCode(UUID.randomUUID().toString());
        testUser.setRoles(Collections.singleton(Role.USER));
        userRepo.save(testUser);
    }

    @Test
    public void testFindByUsername() {
        User user = userRepo.findByUsername("testuser");
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testFindByActivationCode() {
        User user = userRepo.findByActivationCode(testUser.getActivationCode());
        assertNotNull(user);
        assertEquals(testUser.getActivationCode(), user.getActivationCode());
    }

    @Test
    public void testFindByUsername_NotFound() {
        User user = userRepo.findByUsername("nonexistentuser");
        assertNull(user);
    }

    @Test
    public void testFindByActivationCode_NotFound() {
        User user = userRepo.findByActivationCode("nonexistentcode");
        assertNull(user);
    }
}