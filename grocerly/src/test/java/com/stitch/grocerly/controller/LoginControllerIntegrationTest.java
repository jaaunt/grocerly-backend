package com.stitch.grocerly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.grocerly.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST LoginController jaoks
 * Kasutab AbstractIntegrationTest - Testcontainers setup on seal!
 */
@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_WithValidCredentials_Returns200() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "testuser",
                      "password": "password123"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_WithInvalidUsername_ReturnsError() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "wronguser",
                      "password": "password123"
                    }
                    """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void login_WithInvalidPassword_ReturnsError() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "testuser",
                      "password": "wrongpassword"
                    }
                    """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void login_WithEmptyUsername_ReturnsError() throws Exception {
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "username": "",
                      "password": "password123"
                    }
                    """))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void login_WithInvalidJson_ReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}