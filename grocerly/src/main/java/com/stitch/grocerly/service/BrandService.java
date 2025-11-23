package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.BrandResponseDto;
import com.stitch.grocerly.mapper.BrandMapper;
import com.stitch.grocerly.reprository.BrandEntity;
import com.stitch.grocerly.reprository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public BrandResponseDto getBrand(Integer id){
        BrandEntity brandEntity = brandRepository.findById(id).get();
        return brandMapper.mapToDto(brandEntity);
    }

    public List<BrandResponseDto> getAllBrand() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::mapToDto)
                .collect(Collectors.toList());
    }

}
