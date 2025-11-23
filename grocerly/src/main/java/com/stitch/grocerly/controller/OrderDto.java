package com.stitch.grocerly.controller;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class OrderDto {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String orderItems;
    private BigDecimal totalPrice;
}
