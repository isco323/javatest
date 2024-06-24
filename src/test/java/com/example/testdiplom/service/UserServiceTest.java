package com.example.testdiplom.service;

import com.example.testdiplom.domain.Role;
import com.example.testdiplom.domain.User;
import com.example.testdiplom.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private MailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("testuser@example.com");
    }

    @Test
    public void testFindByUsername() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        UserDetails foundUser = userService.loadUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testfindByActivationCode() {
        when(userRepo.findByUsername("testuser")).thenReturn(user);

        boolean result = userService.addUser(user);

        assertFalse(result);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void testNotFound() {
        when(userRepo.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        boolean result = userService.addUser(user);

        assertTrue(result);
        assertTrue(user.isActive());
        assertEquals(1, user.getRoles().size());
        assertNotNull(user.getActivationCode());
        assertEquals("encodedPassword", user.getPassword());

        verify(userRepo, times(1)).save(user);
        verify(mailSender, times(1)).send(eq(user.getEmail()), eq("Activation code"), anyString());
    }

}