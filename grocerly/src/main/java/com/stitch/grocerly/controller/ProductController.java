package com.stitch.grocerly.controller;

import com.stitch.grocerly.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

// Genereerib konstruktori kõikidele nendele kus on final ees
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;



    @GetMapping("products/{id}")
    public ProductResponseDto hello(@PathVariable Integer id){
        return productService.getProduct(id);

    }

    @GetMapping("/all-products")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProduct();
    }

    // Search + filter endpoint
    @GetMapping("/products")
    public List<ProductResponseDto> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice
    ) {
        return productService.searchProducts(q, minPrice, maxPrice);
    }
}
