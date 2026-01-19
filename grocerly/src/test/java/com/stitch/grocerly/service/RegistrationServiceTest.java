package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.exception.DuplicateResourceException;
import com.stitch.grocerly.mapper.RegistrationMapper;
import com.stitch.grocerly.repository.Users;
import com.stitch.grocerly.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST RegistrationService jaoks
 * FIKSITUD VERSIOON: Kasutab Integer ID'd
 */
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private RegistrationService registrationService;

    private UsersDto userDto;
    private Users userEntity;

    @BeforeEach
    void setUp() {
        // Test DTO
        userDto = new UsersDto();
        userDto.setUsername("newuser");
        userDto.setPassword("password123");
        userDto.setEmail("new@example.com");
        userDto.setPhone("+372987654");
        userDto.setFirstName("New");
        userDto.setLastName("User");

        // Test Entity
        userEntity = new Users();
        userEntity.setId(1);  // ← Integer, mitte Long
        userEntity.setUsername("newuser");
        userEntity.setPassword("password123");
        userEntity.setEmail("new@example.com");
        userEntity.setPhone("+372987654");
        userEntity.setFirst_name("New");
        userEntity.setLast_name("User");
    }

    @Test
    void registerUser_WithUniqueCredentials_Success() {
        // ========== ARRANGE ==========
        when(usersRepository.existsByPhone("+372987654")).thenReturn(false);
        when(usersRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(registrationMapper.mapToEntity(userDto)).thenReturn(userEntity);
        when(usersRepository.save(userEntity)).thenReturn(userEntity);
        when(registrationMapper.mapToDto(userEntity)).thenReturn(userDto);

        // ========== ACT ==========
        UsersDto result = registrationService.registerUser(userDto);

        // ========== ASSERT ==========
        assertNotNull(result, "Tulemus ei tohi olla null");
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("+372987654", result.getPhone());

        // VERIFY
        verify(usersRepository, times(1)).existsByPhone("+372987654");
        verify(usersRepository, times(1)).existsByEmail("new@example.com");
        verify(registrationMapper, times(1)).mapToEntity(userDto);
        verify(usersRepository, times(1)).save(userEntity);
        verify(registrationMapper, times(1)).mapToDto(userEntity);
    }

    @Test
    void registerUser_WithDuplicatePhone_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.existsByPhone("+372987654")).thenReturn(true);

        // ========== ACT & ASSERT ==========
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.registerUser(userDto),
                "Peaks viskama DuplicateResourceException kui telefon on juba olemas"
        );

        assertEquals("Phone number already exists!", exception.getMessage());

        verify(usersRepository, times(1)).existsByPhone("+372987654");
        verify(usersRepository, never()).existsByEmail(any());
        verify(usersRepository, never()).save(any());
        verify(registrationMapper, never()).mapToEntity(any());
    }

    @Test
    void registerUser_WithDuplicateEmail_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.existsByPhone("+372987654")).thenReturn(false);
        when(usersRepository.existsByEmail("new@example.com")).thenReturn(true);

        // ========== ACT & ASSERT ==========
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.registerUser(userDto)
        );

        assertEquals("Email already exists!", exception.getMessage());

        verify(usersRepository, times(1)).existsByPhone("+372987654");
        verify(usersRepository, times(1)).existsByEmail("new@example.com");
        verify(usersRepository, never()).save(any());
    }

    @Test
    void registerUser_WithDuplicatePhoneAndEmail_ThrowsPhoneException() {
        // ========== ARRANGE ==========
        when(usersRepository.existsByPhone("+372987654")).thenReturn(true);

        // ========== ACT & ASSERT ==========
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> registrationService.registerUser(userDto)
        );

        assertEquals("Phone number already exists!", exception.getMessage());

        verify(usersRepository, times(1)).existsByPhone("+372987654");
        verify(usersRepository, never()).existsByEmail(any());
    }

    @Test
    void registerUser_WhenSaveFails_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.existsByPhone("+372987654")).thenReturn(false);
        when(usersRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(registrationMapper.mapToEntity(userDto)).thenReturn(userEntity);
        when(usersRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        // ========== ACT & ASSERT ==========
        assertThrows(
                RuntimeException.class,
                () -> registrationService.registerUser(userDto),
                "Peaks propageerima database exception'i"
        );

        verify(usersRepository, times(1)).save(any());
        verify(registrationMapper, never()).mapToDto(any());
    }
}