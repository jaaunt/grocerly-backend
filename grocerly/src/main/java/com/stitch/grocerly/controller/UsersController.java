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

    @GetMapping("/users/{id}")  // http://localhost:8080/api/users/id
    public UsersResponseDto getUser(@PathVariable Integer id) {
        return usersService.getUsers(id);  // kui returnib on 200 ok
    }

    // all users temp vaate jaoks
    @GetMapping("/all-users") // http://localhost:8080/api/all-users
    public List<UsersResponseDto> getAllUsers() {
        return usersService.getAllUsers();   // kui returnib on 200 ok
    }

    // PUT /api/users/{id} - Uuendab konkreetse kasutaja andmeid
    // Kasutatakse profiili muutmise funktsionaalsuse jaoks
    @PutMapping("/users/{id}")   // Kristini lisatud
    public UsersResponseDto updateUser(@PathVariable Integer id, @RequestBody UsersDto usersDto) {
        return usersService.updateUser(id, usersDto);
    }
}
