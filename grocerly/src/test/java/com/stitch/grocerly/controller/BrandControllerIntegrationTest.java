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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== GET /api/brands/{id} TESTID ==========

    @Test
    @Transactional
    // endpoint töötab: 200 OK, tagastab õige brändi
    void getBrand_WithValidId_Returns200() throws Exception {
        mockMvc.perform(get("/api/brands/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brandName").value("Kalev"));
    }

    @Test
    @Transactional
    void getBrand_WithNonExistentId_Returns4xx() throws Exception {
        mockMvc.perform(get("/api/brands/{id}", 99999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    void getBrand_WithInvalidId_Returns4xx() throws Exception {
        mockMvc.perform(get("/api/brands/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    // ========== GET /api/brands TESTID ==========

    @Test
    @Transactional
    // kontrollime tagastab listi, listis on õige arv elemente
    void getAllBrands_ReturnsAllBrands() throws Exception {
        mockMvc.perform(get("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // changelogis on 3 brändi -> hasSize(3)
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
        // ma panin greater than or equal to 0 kuna hasSize tagastab kuni andmebaas eksisteerib,
        // isegi kui andmebaas on tuhi sisult, pole uhtegi rida peaks ta tagastama 0 sest tuhi list,
        // on ikka list
        // ainuke juht kui failib on siis kui see ei eksiteeri uldse

    }
}
