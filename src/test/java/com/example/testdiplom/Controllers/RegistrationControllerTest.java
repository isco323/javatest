package com.example.testdiplom.Controllers;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.testdiplom.domain.User;
import com.example.testdiplom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@WebMvcTest(RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegistrationGet() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testAddUserPasswordMismatch() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("username", "testuser")
                        .param("password", "password1")
                        .param("password2", "password2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("passwordError"))
                .andExpect(view().name("registration"));
    }

    @Test
    public void testAddUserBindingErrors() throws Exception {
        mockMvc.perform(post("/registration")
                        .param("username", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    public void testAddUserUserExists() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/registration")
                        .param("username", "testuser")
                        .param("password", "password")
                        .param("password2", "password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usernameError"))
                .andExpect(view().name("registration"));
    }

    @Test
    public void testAddUserSuccess() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/registration")
                        .param("username", "testuser")
                        .param("password", "password")
                        .param("password2", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void testActivateUserSuccess() throws Exception {
        when(userService.activateUser("code")).thenReturn(true);

        mockMvc.perform(get("/activate/code"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "User activated successfully"))
                .andExpect(view().name("login"));
    }

    @Test
    public void testActivateUserFailure() throws Exception {
        when(userService.activateUser("code")).thenReturn(false);

        mockMvc.perform(get("/activate/code"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Activation code is not found"))
                .andExpect(view().name("login"));
    }
}