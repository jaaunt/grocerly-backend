package com.stitch.grocerly.controller;

import com.stitch.grocerly.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<UsersDto> login(@RequestBody LoginRequestDto request) {
        UsersDto user = loginService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(user);  // 200 ok
    }
}
