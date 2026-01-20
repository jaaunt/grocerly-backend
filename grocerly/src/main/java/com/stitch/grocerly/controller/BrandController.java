package com.stitch.grocerly.controller;

import com.stitch.grocerly.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
// Genereerib konstruktori kõikidele nendele kus on final ees
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("brands/{id}")
    public BrandResponseDto getBrand(@PathVariable Integer id){
        // peab leidma konkreetse brändi ID järgi
        return brandService.getBrand(id);

    }

    @GetMapping("/brands")
    public List<BrandResponseDto> getAllBrands() {
        // tagastama kõik brändid
        return brandService.getAllBrand();
    }
}
