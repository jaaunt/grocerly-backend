package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.UsersResponseDto;
import com.stitch.grocerly.mapper.UsersMapper;
import com.stitch.grocerly.repository.Users;

import com.stitch.grocerly.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersMapper usersMapper;

    private final UsersRepository usersRepository;

    public UsersResponseDto getUsers(Integer id){
        Users users = usersRepository.findById(id).get();
        return usersMapper.mapToDto(users);
    }

    public List<UsersResponseDto> getAllUsers() {
        return usersRepository.findAll()
                .stream()
                .map(usersMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
