package com.stitch.grocerly.reprository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter @Table(name = "product")
public class ProductEntity {
    @Id
    private Integer id;

    private String product_name;

    private String product_description;

    private Float price;

    private Integer product_quantity;

    private String picture;

    @ManyToOne // Tähendab, et mitu produkti võib kuuluda ühele brandile
    @JoinColumn(name = "brand_id") // Andmebaasis on product tabelis väli nimega brand_id
    private BrandEntity brand;
}
