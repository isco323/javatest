package com.example.testdiplom.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testGettersAndSetters() {
        User user = new User();

        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());

        user.setPassword("password");
        assertEquals("password", user.getPassword());

        user.setPassword2("password2");
        assertEquals("password2", user.getPassword2());

        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());

        user.setActivationCode("12345");
        assertEquals("12345", user.getActivationCode());

        user.setActive(true);
        assertTrue(user.isActive());

        Set<Role> roles = Collections.singleton(Role.USER);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }

    @Test
    public void testUserDetailsMethods() {
        User user = new User();
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
    }

    @Test
    public void testValidationConstraints() {
        User user = new User();

        // Username cannot be blank
        user.setUsername("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        // Password cannot be blank
        user.setPassword("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        // Password2 cannot be blank
        user.setPassword2("");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());

        // Email validation
        user.setEmail("notanemail");
        violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setPassword2("password");
        user.setEmail("test@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}