package com.stitch.grocerly.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name= "users")
@Entity
@Getter @Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String first_name;
    @Column(columnDefinition = "TEXT")
    private String last_name;
    @Column(columnDefinition = "TEXT")
    private String username;
    @Column(columnDefinition = "TEXT")
    private String email;
    @Column(columnDefinition = "TEXT")
    private String password;
    @Column(columnDefinition = "TEXT")
    private String phone;
}
