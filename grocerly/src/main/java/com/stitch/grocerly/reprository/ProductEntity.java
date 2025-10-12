package com.stitch.grocerly.reprository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
