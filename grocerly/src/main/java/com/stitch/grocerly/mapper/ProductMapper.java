package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.ProductDto;
import com.stitch.grocerly.controller.ProductResponseDto;
import com.stitch.grocerly.reprository.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

//import org.springframework.web.bind.annotation.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    // mappib DTOks
    @Mapping(source = "id", target = "id")
    @Mapping(source = "product_name", target = "productName")
    @Mapping(source = "product_description", target = "productDescription")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "product_quantity", target = "productQuantity")
    @Mapping(source = "brand.id", target = "brandId") // võtab ProductEntity-st brand objekti ja sealt id

    ProductResponseDto mapToDto(ProductEntity productEntity);

    // Request DTO -> Entity (POST/PUT body -> entity)
    @Mapping(source = "id", target = "id") // ID genereerib DB
    @Mapping(source = "productName",        target = "product_name")
    @Mapping(source = "productDescription", target = "product_description")
    @Mapping(source = "price",              target = "price")
    @Mapping(source = "productQuantity",    target = "product_quantity")
    @Mapping(source = "brandId", target = "brand.id") // Paneb brandId ProductEntity brand objekti sisse
    ProductEntity mapToEntity(ProductDto dto);

}
