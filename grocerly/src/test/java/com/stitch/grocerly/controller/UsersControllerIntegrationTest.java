package com.stitch.grocerly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.grocerly.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST UsersController jaoks
 * Testib GET /users/{id} ja PUT /users/{id} endpointe
 */
@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== GET /users/{id} TESTID ==========

    @Test
    @Transactional
    void getUser_WithValidId_Returns200() throws Exception {
        // Kasutame changelog'ist ID 1001 kasutajat
        mockMvc.perform(get("/api/users/{id}", 1001)
                        // Ütleb serverile, et saadame JSON andmeid
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1001))
                .andExpect(jsonPath("$.username").value("kristin_test"))
                .andExpect(jsonPath("$.email").value("kristin@test.com"))
                .andExpect(jsonPath("$.phone").value("+372555111"))
                .andExpect(jsonPath("$.firstName").value("Kristin"))
                .andExpect(jsonPath("$.lastName").value("Testija"));
    }

    @Test
    @Transactional
    // Kontrollida, et olematu kasutaja otsimisel tuleb 404 viga!
    void getUser_WithNonExistentId_Returns404() throws Exception {
        // Proovime ID-d mis ei eksisteeri
        mockMvc.perform(get("/api/users/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    // Kontrollida, et negatiivse ID-ga päring annab vea
    void getUser_WithInvalidId_Returns4xx() throws Exception {
        // Negatiivne ID või vale formaat
        mockMvc.perform(get("/api/users/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                // is4xxClientError() = mistahes 4xx viga (400, 404, 422, jne)
                .andExpect(status().is4xxClientError());
    }

    // ========== PUT /users/{id} TESTID ==========

    @Test
    @Transactional
    // Kontrollida, et kasutaja andmete uuendamine töötab õigesti
    void updateUser_WithValidData_Returns200() throws Exception {
        // Uuendame ID 1002 kasutaja andmeid
        // put() - HTTP PUT meetod (uuendamine)
        mockMvc.perform(put("/api/users/{id}", 1002)
                        // Ütleb serverile, et saadame JSON andmeid
                        .contentType(MediaType.APPLICATION_JSON)
                        // See JSON läheb HTTP päringu body'sse
                        // Spring teisendab selle UsersDto objektiks
                        //Väljad peavad vastama UsersDto klassile!
                        .content("""
                    {
                      "userId": 1002,
                      "username": "updated_kristin",
                      "email": "updated@test.com",
                      "phone": "+372999888",
                      "firstName": "Updated",
                      "lastName": "Name"
                    }
                    """))
                // Kontrollib, et uuendamine õnnestus (HTTP 200)
                .andExpect(status().isOk())
                // Kontrollib, et vastuses on uuendatud andmed
                .andExpect(jsonPath("$.userId").value(1002))
                .andExpect(jsonPath("$.username").value("updated_kristin"))
                .andExpect(jsonPath("$.email").value("updated@test.com"))
                .andExpect(jsonPath("$.phone").value("+372999888"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"));
    }

    @Test
    @Transactional
    // Kontrollida, et olematu kasutaja uuendamine annab 404 vea
    void updateUser_WithNonExistentId_Returns404() throws Exception {
        // Proovime uuendada kasutajat, kes ei eksisteeri
        mockMvc.perform(put("/api/users/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "userId": 99999,
                      "username": "doesnotmatter",
                      "email": "test@example.com",
                      "phone": "+372111222",
                      "firstName": "Test",
                      "lastName": "User"
                    }
                    """))
                // Kontrollib, et tuli 404 viga
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    // Kontrollida, et tühi username ei ole lubatud
    void updateUser_WithEmptyUsername_ReturnsBadRequest() throws Exception {
        // Tühi username ei tohiks olla lubatud
        mockMvc.perform(put("/api/users/{id}", 1002)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "userId": 1002,
                      "username": "",
                      "email": "test@example.com",
                      "phone": "+372111222",
                      "firstName": "Test",
                      "lastName": "User"
                    }
                    """))
                // status().isBadRequest() = HTTP 400
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    // Kontrollida, et email validatsioon töötab! Et oleks korektne email
    void updateUser_WithInvalidEmail_ReturnsBadRequest() throws Exception {
        // Vale email formaat
        mockMvc.perform(put("/api/users/{id}", 1002)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "userId": 1002,
                      "username": "validuser",
                      "email": "not-an-email",
                      "phone": "+372111222",
                      "firstName": "Test",
                      "lastName": "User"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    // Kontrollida, et vale JSON süntaks annab vea!
    void updateUser_WithInvalidJson_ReturnsBadRequest() throws Exception {
        // Vale JSON formaat. See ei ole korrektne JSON süntaks!
        //Puuduvad jutumärgid, komad
        String invalidJson = "{invalid json here}";

        mockMvc.perform(put("/api/users/{id}", 1002)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}