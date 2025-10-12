package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.exception.DuplicateResourceException;
import com.stitch.grocerly.mapper.RegistrationMapper;
import com.stitch.grocerly.repository.UsersRepository;
import com.stitch.grocerly.repository.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final RegistrationMapper registrationMapper;

    public UsersDto registerUser(UsersDto dto) {
        if(usersRepository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Phone number already exists!");
        }
        if(usersRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists!");
        }
        Users user = registrationMapper.mapToEntity(dto);
        Users saved = usersRepository.save(user);
        return registrationMapper.mapToDto(saved);
    }
}