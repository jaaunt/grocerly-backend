package com.stitch.grocerly.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stitch.grocerly.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST OrderController jaoks
 *
 * Endpoints:
 * - POST /api/orders
 * - GET /api/orders
 * - GET /api/orders/user/{userId}
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_WithValidData_ReturnsOk() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(998L);
        orderDto.setName("Jane Doe");
        orderDto.setEmail("jane@example.com");
        orderDto.setPhone("+372111222");
        orderDto.setAddress("Tartu, Estonia");
        orderDto.setOrderItems("Kunafa pistaatsiakreemiga shokolaad x 4, Vanini sokolaadide valik x 3");
        orderDto.setTotalPrice(new BigDecimal("49.99"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(998))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.totalPrice").value(49.99));
    }

    @Test
    void getAllOrders_ReturnsAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void getOrdersByUser_ReturnsUserOrders() throws Exception {
        // First create an order for user 998
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(998L);
        orderDto.setName("Test User");
        orderDto.setEmail("testorder@example.com");
        orderDto.setPhone("+372123456");
        orderDto.setAddress("Tallinn, Estonia");
        orderDto.setOrderItems("Kunafa pistaatsiakreemiga shokolaad x 4, Vanini sokolaadide valik x 3");
        orderDto.setTotalPrice(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk());

        // Then get orders for user 998
        mockMvc.perform(get("/api/orders/user/998"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void getOrdersByUser_WithNoOrders_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/orders/user/999999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createOrder_WithMissingFields_ReturnsError() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(998L);
        // Missing required fields

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createOrder_WithInvalidJson_ReturnsBadRequest() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMultipleOrders_Success() throws Exception {
        OrderDto order1 = new OrderDto();
        order1.setUserId(998L);
        order1.setName("Order 1");
        order1.setEmail("order1@example.com");
        order1.setPhone("+372111111");
        order1.setAddress("Address 1");
        order1.setOrderItems("Kunafa pistaatsiakreemiga shokolaad x 4, Vanini sokolaadide valik x 3");
        order1.setTotalPrice(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isOk());

        OrderDto order2 = new OrderDto();
        order2.setUserId(998L);
        order2.setName("Order 2");
        order2.setEmail("order2@example.com");
        order2.setPhone("+372222222");
        order2.setAddress("Address 2");
        order2.setOrderItems("Kunafa pistaatsiakreemiga shokolaad x 4, Vanini sokolaadide valik x 3");
        order2.setTotalPrice(new BigDecimal("20.00"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order2)))
                .andExpect(status().isOk());

        // Verify both orders exist
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void createOrder_WithNullUserId_HandlesGracefully() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(null);
        orderDto.setName("Test");
        orderDto.setEmail("test@example.com");
        orderDto.setPhone("+372123456");
        orderDto.setAddress("Test Address");
        orderDto.setOrderItems("[]");
        orderDto.setTotalPrice(new BigDecimal("0.00"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isBadRequest());
    }
}