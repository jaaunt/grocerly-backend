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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST ProductController jaoks
 * Testib GET /products/{id} ja GET /products (search) endpointe
 * Kasutab AbstractIntegrationTest - Testcontainers setup on seal!
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== GET /api/products/{id} TESTID ==========

    @Test
    @Transactional
    void getProduct_WithValidId_Returns200() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 3001)
                        // Ütleb MockMvc-le ja serverile, et me ootame JSON formaadis vastust
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3001))
                .andExpect(jsonPath("$.productName").value("Test Laptop"))
                .andExpect(jsonPath("$.productDescription").value("High performance laptop for testing"))
                .andExpect(jsonPath("$.price").value("999.99"))
                .andExpect(jsonPath("$.productQuantity").value("50"))
                .andExpect(jsonPath("$.brandId").value(2001))
                .andExpect(jsonPath("$.picture").value("laptop.jpg"));
    }

    @Test
    @Transactional
    // Kontrollida, et kui toodet ei leita, siis tuleb õige veateade!
    void getProduct_WithNonExistentId_Returns404() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 99999)
                .contentType(MediaType.APPLICATION_JSON))
                // Kontrollib, et vastus on **täpselt 404. Kuna toodet ID-ga 99999 ei eksisteeri
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getProduct_WithInvalidId_Returns4xx() throws Exception {
        // ID = -1 (negatiivne - vale sisend!)
        mockMvc.perform(get("/api/products/{id}", -1)
                .contentType(MediaType.APPLICATION_JSON))
                // Kontrollib: .is4xxClientError() → mistahes 4xx on ok (400, 404, 422, jne)
                .andExpect(status().is4xxClientError());
    }

    // ========== GET /api/products (SEARCH) TESTID ==========

    @Test
    @Transactional
    void searchProducts_WithNoFilters_ReturnsAllProducts() throws Exception {
        // Ilma filtriteta peaks tagastama kõik 3 toodet changelog'ist
        mockMvc.perform(get("/api/products")
                .contentType((MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                // Kuna tagastatakse List<ProductResponseDto>, siis JSON on array
                .andExpect(jsonPath("$", hasSize(3)));  // hasSize Kontrollib, et array'is on TÄPSELT 3 elementi. 3 sest 3 changeSet'i = 3 toodet = hasSize(3)
    }

    @Test
    @Transactional
    void searchProducts_WithQueryParam_ReturnsFilteredProducts() throws Exception {
        // Otsime "laptop" - peaks leidma ainult ühe toote
        mockMvc.perform(get("/api/products")
                        // Lisab URL-ile query parameetri. URL muutub: /api/products → /api/products?q=laptop
                        .param("q", "laptop")  // ← Query parameeter!
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))  // Ainult 1 toode
                // jsonPath("$[0].productName")` = Mine array'i esimesse elementi ja võta productName väli
                // .value("Test Laptop")` = Kontrolli, et see on "Test Laptop"
                .andExpect(jsonPath("$[0].productName").value("Test Laptop"));
    }

    @Test
    @Transactional
    // Ainult minPrice, maxPrice puudub, Filtreerib hinna järgi
    void searchProducts_WithMinPriceOnly_ReturnsFilteredProducts() throws Exception {
        // Otsime tooteid, mis maksavad üle 100
        mockMvc.perform(get("/api/products")
                        // Lisab URL-ile query parameetri: ?minPrice=100. URL: /api/products?minPrice=100
                        .param("minPrice", "100")  // ← Min hind!
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));  // 2 toodet: Laptop + Keyboard
    }

    }

