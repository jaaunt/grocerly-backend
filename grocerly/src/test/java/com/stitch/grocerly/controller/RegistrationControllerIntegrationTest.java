package com.stitch.grocerly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.grocerly.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST RegistrationController jaoks
 *
 * Endpoint: POST /api/register
 * Expected: 201 CREATED
 */
@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_WithValidData_Returns201Created() throws Exception {
        UsersDto userDto = new UsersDto();
        userDto.setUsername("newuser");
        userDto.setPassword("password123");
        userDto.setEmail("new@example.com");
        userDto.setPhone("+372987654");
        userDto.setFirstName("New");
        userDto.setLastName("User");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.phone").value("+372987654"));
    }

    @Test
    void register_WithDuplicateUsername_ReturnsError() throws Exception {
        // First registration
        UsersDto userDto1 = new UsersDto();
        userDto1.setUsername("duplicateuser");
        userDto1.setPassword("password123");
        userDto1.setEmail("user1@example.com");
        userDto1.setPhone("+372111111");
        userDto1.setFirstName("User");
        userDto1.setLastName("One");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1)))
                .andExpect(status().isCreated());

        // Try to register with same username
        UsersDto userDto2 = new UsersDto();
        userDto2.setUsername("duplicateuser"); // Same username
        userDto2.setPassword("password456");
        userDto2.setEmail("user2@example.com");
        userDto2.setPhone("+372222222");
        userDto2.setFirstName("User");
        userDto2.setLastName("Two");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto2)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void register_WithDuplicateEmail_ReturnsError() throws Exception {
        // First registration
        UsersDto userDto1 = new UsersDto();
        userDto1.setUsername("user1");
        userDto1.setPassword("password123");
        userDto1.setEmail("duplicate@example.com");
        userDto1.setPhone("+372333333");
        userDto1.setFirstName("User");
        userDto1.setLastName("One");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto1)))
                .andExpect(status().isCreated());

        // Try to register with same email
        UsersDto userDto2 = new UsersDto();
        userDto2.setUsername("user2");
        userDto2.setPassword("password456");
        userDto2.setEmail("duplicate@example.com"); // Same email
        userDto2.setPhone("+372444444");
        userDto2.setFirstName("User");
        userDto2.setLastName("Two");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto2)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void register_WithMissingUsername_ReturnsError() throws Exception {
        UsersDto userDto = new UsersDto();
        // Username missing
        userDto.setPassword("password123");
        userDto.setEmail("test@example.com");
        userDto.setPhone("+372555555");
        userDto.setFirstName("Test");
        userDto.setLastName("User");

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void register_WithInvalidJson_ReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}