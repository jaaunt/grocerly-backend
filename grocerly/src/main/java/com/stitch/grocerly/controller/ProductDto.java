package com.stitch.grocerly.controller;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String productName;
    private String productDescription;
    private String price;
    private String productQuantity;


}
