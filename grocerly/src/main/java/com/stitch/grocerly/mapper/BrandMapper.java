package com.stitch.grocerly.mapper;

import com.stitch.grocerly.controller.BrandDto;
import com.stitch.grocerly.controller.BrandResponseDto;
import com.stitch.grocerly.reprository.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper {
    // Entity -> Response DTO (kui loeme andmebaasist)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "brand_name", target = "brandName")
    BrandResponseDto mapToDto(BrandEntity brandEntity);

    // Request DTO -> Entity (kui salvestame andmebaasi)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "brandName", target = "brand_name")
    BrandEntity mapToEntity(BrandDto dto);
}
