package com.stitch.grocerly.reprository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    // Search meetod, otsib sarnast nime, concat liidab stringe, lower teeb undercaseiks et poleks case sensitive
    // % ette ja taha et see sona pikkus ei loeks a la otsime piim siis match on ka piim ja shokolaadipiim, ilma poleks
    // shokolaadipiim match
    @Query("SELECT p FROM ProductEntity p WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(p.product_name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.product_description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<ProductEntity> searchProductsWithPriceFilter(
            @Param("search") String search,
            @Param("minPrice") Float minPrice,
            @Param("maxPrice") Float maxPrice
    );
}
