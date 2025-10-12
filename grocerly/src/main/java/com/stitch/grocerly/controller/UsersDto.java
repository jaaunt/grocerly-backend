package com.stitch.grocerly.controller;

import lombok.Data;

@Data  // teeb getter, setter, equals, hashcode
public class UsersDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String phone;

}
