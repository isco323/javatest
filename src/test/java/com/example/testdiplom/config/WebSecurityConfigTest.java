package com.example.testdiplom.config;
import com.example.testdiplom.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    public void passwordEncoderBean() {
        String rawPassword = "password";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assert passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Test
    public void givenPublicUrl_whenGetRequest_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/activate/some-code"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenCustomLoginPage_whenGetLogin_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk());
    }

}