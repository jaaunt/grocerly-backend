package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.BrandResponseDto;
import com.stitch.grocerly.mapper.BrandMapper;
import com.stitch.grocerly.reprository.BrandEntity;
import com.stitch.grocerly.reprository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock  // Loob VÕLTS BrandRepository
    private BrandRepository brandRepository;

    @Mock  // Loob VÕLTS BrandMapper
    private BrandMapper brandMapper;

    @InjectMocks  // Loob PÄRIS BrandService
    private BrandService brandService;

    @Test
    void getBrand() {
        // Testib getBrand(Integer id) meetodit - tagastab ühe brändi ID järgi

        // given
        Integer brandId = 1;

        BrandEntity fakeBrandEntity = new BrandEntity();
        fakeBrandEntity.setId(1);
        fakeBrandEntity.setBrand_name("Nike");

        BrandResponseDto expectedDto = new BrandResponseDto();
        expectedDto.setId(1);
        expectedDto.setBrandName("Nike");

        given(brandRepository.findById(brandId))
                .willReturn(Optional.of(fakeBrandEntity));

        given(brandMapper.mapToDto(fakeBrandEntity))
                .willReturn(expectedDto);

        // when
        BrandResponseDto result = brandService.getBrand(brandId);

        // then
        then(brandRepository).should().findById(brandId);
        then(brandMapper).should().mapToDto(fakeBrandEntity);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getBrandName(), result.getBrandName());

    }

    @Test
    void getAllBrand() {
        // Testib getAllBrand() meetodit - tagastab kõik brändid listina

        // given
        BrandEntity brand1 = new BrandEntity();
        brand1.setId(1);
        brand1.setBrand_name("Nike");

        BrandEntity brand2 = new BrandEntity();
        brand2.setId(2);
        brand2.setBrand_name("Adidas");

        BrandEntity brand3 = new BrandEntity();
        brand3.setId(3);
        brand3.setBrand_name("Puma");

        List<BrandEntity> fakeBrandEntities = List.of(brand1, brand2, brand3);

        BrandResponseDto dto1 = new BrandResponseDto();
        dto1.setId(1);
        dto1.setBrandName("Nike");

        BrandResponseDto dto2 = new BrandResponseDto();
        dto2.setId(2);
        dto2.setBrandName("Adidas");

        BrandResponseDto dto3 = new BrandResponseDto();
        dto3.setId(3);
        dto3.setBrandName("Puma");

        // õpetab mock objekte, mida tagastada kui neid kutsutakse
        given(brandRepository.findAll()).willReturn(fakeBrandEntities);
        given(brandMapper.mapToDto(brand1)).willReturn(dto1);
        given(brandMapper.mapToDto(brand2)).willReturn(dto2);
        given(brandMapper.mapToDto(brand3)).willReturn(dto3);

        // when
        List<BrandResponseDto> result = brandService.getAllBrand();

        // then
        then(brandRepository).should().findAll();
        then(brandMapper).should().mapToDto(brand1);
        then(brandMapper).should().mapToDto(brand2);
        then(brandMapper).should().mapToDto(brand3);

        assertNotNull(result);
        // assertEquals(oodatud, tegelik) => Kontrolli, et oodatud väärtus võrdub tegeliku väärtusega
        assertEquals(3, result.size());
        assertEquals("Nike", result.get(0).getBrandName());
        assertEquals("Adidas", result.get(1).getBrandName());
        assertEquals("Puma", result.get(2).getBrandName());
    }
}
