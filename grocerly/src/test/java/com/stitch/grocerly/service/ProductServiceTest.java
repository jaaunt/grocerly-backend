package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.ProductResponseDto;
import com.stitch.grocerly.mapper.ProductMapper;
import com.stitch.grocerly.reprository.ProductEntity;
import com.stitch.grocerly.reprository.ProductRepository;
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
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock  // Loob VÕLTS ProductRepository
    private ProductRepository productRepository;

    @Mock  // Loob VÕLTS ProductMapper
    private ProductMapper productMapper;

    @InjectMocks  // Loob PÄRIS ProductService
    private ProductService productService;

    @Test
    void getProduct() {
        // Testib getProduct(Integer id) meetodit - tagastab ühe toote ID järgi

        // given - Paneme süsteemi teadaolevasse olekusse
        Integer productId = 1;

        ProductEntity fakeProductEntity = new ProductEntity();
        fakeProductEntity.setId(1);
        fakeProductEntity.setProduct_name("Bitter šokolaad");
        fakeProductEntity.setProduct_description("70% tume šokolaad");
        // float seega lühendina f
        fakeProductEntity.setPrice(2.07f);
        fakeProductEntity.setProduct_quantity(7);

        ProductResponseDto expectedDto = new ProductResponseDto();
        expectedDto.setId(1);
        expectedDto.setProductName("Bitter šokolaad");
        expectedDto.setProductDescription("70% tume šokolaad");
        expectedDto.setPrice("2.07");
        expectedDto.setProductQuantity("7");
        expectedDto.setBrandId(1);

        // Kui küsime repositooriumist toote ID 1, siis tagastab fakeProductEntity
        given(productRepository.findById(productId))
                .willReturn(Optional.of(fakeProductEntity));

        // Kui mapper saab fakeProductEntity, siis tagastab expectedDto
        given(productMapper.mapToDto(fakeProductEntity))
                .willReturn(expectedDto);

        // when - Tegevus mida tahame testida
        ProductResponseDto result = productService.getProduct(productId);

        // then - Kontrollime, et süsteem käitus nagu ootasime
        // Kontrollime, et kutsuti õigeid meetodeid
        then(productRepository).should().findById(productId);
        then(productMapper).should().mapToDto(fakeProductEntity);

        // Kontrollime, et tulemus on õige
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getProductName(), result.getProductName());
        assertEquals(expectedDto.getProductDescription(), result.getProductDescription());
        assertEquals(expectedDto.getPrice(), result.getPrice());
        assertEquals(expectedDto.getProductQuantity(), result.getProductQuantity());
    }



    @Test
    void searchProducts() {
        // given
        String query = "šokolaad";
        Float minPrice = 2.0f;
        Float maxPrice = 4.0f;

        // Loome TÜHJA objekti
        ProductEntity product1 = new ProductEntity();
        // Määrame väärtused setter'itega
        product1.setId(1);
        product1.setProduct_name("Bitter šokolaad");
        product1.setProduct_description("70% tume šokolaad");
        product1.setPrice(2.0f);
        product1.setProduct_quantity(7);

        ProductEntity product2 = new ProductEntity();
        // Määrame väärtused setter'itega
        product2.setId(2);
        product2.setProduct_name("Kunafa šokolaad");
        product2.setProduct_description("Look me Dubai šokolaad");
        product2.setPrice(3.0f);
        product2.setProduct_quantity(17);

        // Loome võlts-listi, mille repository tagastab kui kutsutakse searchProductsWithPriceFilter()
        List<ProductEntity> fakeProductEntities = List.of(product1, product2);

        ProductResponseDto dto1 = new ProductResponseDto();
        dto1.setId(1);
        dto1.setProductName("Bitter šokolaad");
        dto1.setProductDescription("70% tume šokolaad");
        dto1.setPrice("2.0");
        dto1.setProductQuantity("7");

        ProductResponseDto dto2 = new ProductResponseDto();
        dto2.setId(2);
        dto2.setProductName("Kunafa šokolaad");
        dto2.setProductDescription("Look me Dubai šokolaad");
        dto2.setPrice("3.0");
        dto2.setProductQuantity("17");


        // Õpetab mock Repositoryt: tagastab fakeProductEntities listi  given
        given(productRepository.searchProductsWithPriceFilter(query, minPrice, maxPrice)).willReturn(fakeProductEntities);

        // Mapper kutsutakse 2 korda, iga entity jaoks
        // Õpetame mapper mock'i: kui saab product1, tagasta dto1
        given(productMapper.mapToDto(product1)).willReturn(dto1);
        given(productMapper.mapToDto(product2)).willReturn(dto2);

        // when tegevus mida tahame testida
        List<ProductResponseDto> result = productService.searchProducts(query, minPrice, maxPrice);

        // then - Kontrollime, et et kutsuti õigeid meetodeid
        then(productRepository).should().searchProductsWithPriceFilter(query, minPrice, maxPrice);
        then(productMapper).should().mapToDto(product1);
        then(productMapper).should().mapToDto(product2);

        // Kontrollime, et tulemus on õige
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bitter šokolaad", result.get(0).getProductName());
        assertEquals("Kunafa šokolaad", result.get(1).getProductName());
        assertEquals("2.0", result.get(0).getPrice());
        assertEquals("3.0", result.get(1).getPrice());
    }

    @Test
    void searchProducts_noParams() {
        // given
        String query = null;
        Float minPrice = null;
        Float maxPrice = null;

        ProductEntity product1 = new ProductEntity();
        ProductEntity product2 = new ProductEntity();

        List<ProductEntity> allproducts = List.of(product1, product2);

        ProductResponseDto dto1 = new ProductResponseDto();
        ProductResponseDto dto2 = new ProductResponseDto();


        // Kui keegi kutsub findAll(), siis tagasta see minu loodud list allproducts
        given(productRepository.findAll()).willReturn(allproducts);
        given(productMapper.mapToDto(product1)).willReturn(dto1);
        given(productMapper.mapToDto(product2)).willReturn(dto2);

        // when tegevus mida tahame testida nende sisenditega, mille sa GIVEN-is paika panid
        List<ProductResponseDto> result = productService.searchProducts(query, minPrice, maxPrice);

        // then - Kontrollime, et et kutsuti õigeid meetodeid
        then(productRepository).should().findAll();
        then(productMapper).should().mapToDto(product1);
        then(productMapper).should().mapToDto(product2);
        //then(productRepository).should(never()).searchProductsWithPriceFilter(any(), any(), any());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    // katab katab query.trim().isEmpty()
    void searchProducts_emptyQuery() {
        // given
        String query = " ";
        Float minPrice = null;
        Float maxPrice = null;

        ProductEntity product1 = new ProductEntity();
        ProductEntity product2 = new ProductEntity();

        List<ProductEntity> allproducts = List.of(product1, product2);

        ProductResponseDto dto1 = new ProductResponseDto();
        ProductResponseDto dto2 = new ProductResponseDto();

        // Kui keegi kutsub findAll(), siis tagasta see minu loodud list allproducts
        given(productRepository.findAll()).willReturn(allproducts);
        given(productMapper.mapToDto(product1)).willReturn(dto1);
        given(productMapper.mapToDto(product2)).willReturn(dto2);

        // when tegevus mida tahame testida nende sisenditega, mille sa GIVEN-is paika panid
        List<ProductResponseDto> result = productService.searchProducts(query, minPrice, maxPrice);

        // then - Kontrollime, et et kutsuti õigeid meetodeid
        then(productRepository).should().findAll();
        then(productMapper).should().mapToDto(product1);
        then(productMapper).should().mapToDto(product2);

        assertNotNull(result);
        assertEquals(2, result.size());

    }
}