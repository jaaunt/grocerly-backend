package com.stitch.grocerly.controller;

import com.stitch.grocerly.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor  // genereeri structor koigile noudtud arguentidele
@RequestMapping("/api")
public class UsersController {

    private final UsersService usersService;

    @GetMapping("users/{id}")  // http://localhost:8080/users
    public UsersResponseDto users(@PathVariable Integer id) {
        return usersService.getUsers(id);
    }

    @GetMapping("/users")
    public List<UsersResponseDto> getAllUsers() {
        return usersService.getAllUsers();
    }
}
