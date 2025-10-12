package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.LoginRequestDto;
import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.exception.InvalidCredentialsException;
import com.stitch.grocerly.mapper.RegistrationMapper;
import com.stitch.grocerly.repository.Users;
import com.stitch.grocerly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UsersRepository usersRepository;
    private final RegistrationMapper registrationMapper;

    public UsersDto login(LoginRequestDto request) {
        Users user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return registrationMapper.mapToDto(user);
    }
}
