package com.stitch.grocerly.controller;

import com.stitch.grocerly.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<UsersDto> register(@RequestBody UsersDto userDto) {
        UsersDto createdUser = registrationService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);  // 201 created aka post
    }
}
