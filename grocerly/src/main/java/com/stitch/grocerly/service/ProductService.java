package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.ProductResponseDto;
import com.stitch.grocerly.mapper.ProductMapper;
import com.stitch.grocerly.reprository.ProductEntity;
import com.stitch.grocerly.reprository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDto getProduct(Integer id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return productMapper.mapToDto(productEntity);
    }

    public List<ProductResponseDto> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }

    // Search meetod + filter
    public List<ProductResponseDto> searchProducts(String query, Float minPrice, Float maxPrice) {
        // if empty tagasta koik tooted
        if ((query == null || query.trim().isEmpty()) && minPrice == null && maxPrice == null) {
            return getAllProduct();
        }

        // uus meetod price filteriga
        return productRepository.searchProductsWithPriceFilter(query, minPrice, maxPrice)
                .stream()
                .map(productMapper::mapToDto)
                .toList();
    }
}