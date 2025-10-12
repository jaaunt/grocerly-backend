package com.stitch.grocerly.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name= "users")
@Entity
@Getter @Setter
public class Users {
    @Id
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String first_name;
}
