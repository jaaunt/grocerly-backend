package com.stitch.grocerly.controller;

import lombok.Data;

@Data
public class UsersResponseDto {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String username;
    private String phone;

    // all users list jaoks, profiili jaoks
}
