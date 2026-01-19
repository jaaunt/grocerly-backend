package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.controller.UsersResponseDto;
import com.stitch.grocerly.mapper.UsersMapper;
import com.stitch.grocerly.repository.Users;
import com.stitch.grocerly.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UsersMapper usersMapper;

    @InjectMocks
    private UsersService usersService;

    @Test
    void getUsers() {
        Integer id = 1;

        Users fakeUser = new Users();
        fakeUser.setId(1);
        fakeUser.setFirst_name("Kristin");
        fakeUser.setLast_name("Vares");
        fakeUser.setEmail("kristin@example.com");
        fakeUser.setPhone("555");

        // getUsers() tagastab UsersResponseDto
        UsersResponseDto expectedDto = new UsersResponseDto();
        expectedDto.setUserId(1);
        expectedDto.setFirstName("Kristin");
        expectedDto.setLastName("Vares");
        expectedDto.setEmail("kristin@example.com");
        expectedDto.setPhone("555");

        given(usersRepository.findById(id)).willReturn(Optional.of(fakeUser));
        given(usersMapper.mapToDto(fakeUser)).willReturn(expectedDto);

        // when
        UsersResponseDto result = usersService.getUsers(id);

        // then
        then(usersRepository).should().findById(id);
        then(usersMapper).should().mapToDto(fakeUser);

        assertNotNull(result);
        assertEquals(expectedDto.getUserId(), result.getUserId());
        assertEquals(expectedDto.getFirstName(), result.getFirstName());
        assertEquals(expectedDto.getLastName(), result.getLastName());
        assertEquals(expectedDto.getEmail(), result.getEmail());
        assertEquals(expectedDto.getPhone(), result.getPhone());
    }

    @Test
    void getAllUsers() {
        // given
        Users user1 = new Users();
        user1.setId(1);
        user1.setFirst_name("Kristin");

        Users user2 = new Users();
        user2.setId(2);
        user2.setFirst_name("Mari");

        List<Users> usersList = List.of(user1, user2);

        UsersResponseDto dto1 = new UsersResponseDto();
        dto1.setUserId(1);
        dto1.setFirstName("Kristin");

        UsersResponseDto dto2 = new UsersResponseDto();
        dto2.setUserId(2);
        dto2.setFirstName("Mari");

        given(usersRepository.findAll()).willReturn(usersList);
        given(usersMapper.mapToDto(user1)).willReturn(dto1);
        given(usersMapper.mapToDto(user2)).willReturn(dto2);

        // when
        List<UsersResponseDto> result = usersService.getAllUsers();

        // then
        then(usersRepository).should().findAll();
        then(usersMapper).should().mapToDto(user1);
        then(usersMapper).should().mapToDto(user2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
        assertEquals(2, result.get(1).getUserId());

    }

    @Test
    void updateUser_andReturnDto() {
        // given
        Integer id = 1;

        Users existing = new Users();
        existing.setId(id);
        existing.setFirst_name("Vana");
        existing.setLast_name("Nimi");
        existing.setEmail("old@example.com");
        existing.setPhone("000");

        // request body (dto) — uued andmed
        UsersDto dto = new UsersDto();
        dto.setFirstName("Kristin");
        dto.setLastName("Vares");
        dto.setEmail("kristin@example.com");
        dto.setPhone("555");

        // mis repository.save tagastab
        Users saved = new Users();
        saved.setId(id);
        saved.setFirst_name("Kristin");
        saved.setLast_name("Vares");
        saved.setEmail("kristin@example.com");
        saved.setPhone("555");

        // mis controllerile tagasi läheb
        UsersResponseDto expected = new UsersResponseDto();
        expected.setUserId(id);
        expected.setFirstName("Kristin");
        expected.setLastName("Vares");
        expected.setEmail("kristin@example.com");
        expected.setPhone("555");

        given(usersRepository.findById(id)).willReturn(java.util.Optional.of(existing));
        given(usersRepository.save(existing)).willReturn(saved);
        given(usersMapper.mapToDto(saved)).willReturn(expected);

        // when
        UsersResponseDto result = usersService.updateUser(id, dto);

        // then (kontrollime, et kutsuti õigeid asju)
        then(usersRepository).should().findById(id);
        then(usersRepository).should().save(existing);
        then(usersMapper).should().mapToDto(saved);

        // then (kontrollime, et useri väljad said uuendatud ENNE save)
        assertEquals("Kristin", existing.getFirst_name());
        assertEquals("Vares", existing.getLast_name());
        assertEquals("kristin@example.com", existing.getEmail());
        assertEquals("555", existing.getPhone());

        // tulemus
        assertNotNull(result);
        assertEquals(expected.getUserId(), result.getUserId());
        assertEquals(expected.getFirstName(), result.getFirstName());
        assertEquals(expected.getLastName(), result.getLastName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPhone(), result.getPhone());
    }
}