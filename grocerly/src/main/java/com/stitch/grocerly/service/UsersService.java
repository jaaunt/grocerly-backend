package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.UsersResponseDto;
import com.stitch.grocerly.mapper.UsersMapper;
import com.stitch.grocerly.repository.Users;
import com.stitch.grocerly.controller.UsersDto;

import com.stitch.grocerly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;

    public UsersResponseDto getUsers(Integer id) {
        Users users = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return usersMapper.mapToDto(users);
    }

    // all users temp vaate jaoks
    public List<UsersResponseDto> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::mapToDto)
                .toList();
    }

    // Kasutaja andmete uuendamine (eesnimi, perekonnanimi, email, telefon)
    public UsersResponseDto updateUser(Integer id, UsersDto usersDto) {
        // Leia kasutaja
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kasutajat ei leitud ID-ga: " + id));

        // Uuenda väljad
        user.setFirst_name(usersDto.getFirstName());
        user.setLast_name(usersDto.getLastName());
        user.setEmail(usersDto.getEmail());
        user.setPhone(usersDto.getPhone());

        // Salvesta andmebaasi
        Users updatedUser = usersRepository.save(user);

        // Tagasta DTO
        return usersMapper.mapToDto(updatedUser);
    }
}
