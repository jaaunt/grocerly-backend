package com.stitch.grocerly.reprository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter @Table(name = "brand")
public class BrandEntity {
    @Id
    private Integer id;

    private String brand_name;
}
