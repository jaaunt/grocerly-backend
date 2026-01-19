package com.stitch.grocerly.service;

import com.stitch.grocerly.controller.LoginRequestDto;
import com.stitch.grocerly.controller.UsersDto;
import com.stitch.grocerly.exception.InvalidCredentialsException;
import com.stitch.grocerly.mapper.RegistrationMapper;
import com.stitch.grocerly.repository.Users;
import com.stitch.grocerly.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST LoginService jaoks
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RegistrationMapper registrationMapper;

    @InjectMocks
    private LoginService loginService;

    private Users testUser;
    private UsersDto testUserDto;
    private LoginRequestDto loginRequest;

    @BeforeEach
    void setUp() {
        // Loome test kasutaja (entity)
        testUser = new Users();
        testUser.setId(1);  // ← MUUDETUD: Integer, mitte Long
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setPhone("+372123456");

        // Loome test kasutaja DTO
        testUserDto = new UsersDto();
        testUserDto.setId(1);  // ← MUUDETUD: Integer, mitte Long
        testUserDto.setUsername("testuser");
        testUserDto.setEmail("test@example.com");
        testUserDto.setPhone("+372123456");

        // Loome login request
        loginRequest = new LoginRequestDto();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    /**
     * TEST 1: Edukas login
     */
    @Test
    void login_WithValidCredentials_ReturnsUserDto() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        when(registrationMapper.mapToDto(testUser))
                .thenReturn(testUserDto);

        // ========== ACT ==========
        UsersDto result = loginService.login(loginRequest);

        // ========== ASSERT ==========
        assertNotNull(result, "Tulemus ei tohi olla null");

        // ← MUUDETUD: Kasutame Integer.valueOf() et vältida ambiguous call'i
        assertEquals(Integer.valueOf(1), result.getId(), "Kasutaja ID peaks olema 1");
        assertEquals("testuser", result.getUsername(), "Username peaks olema 'testuser'");
        assertEquals("test@example.com", result.getEmail(), "Email peaks olema õige");

        // VERIFY
        verify(usersRepository, times(1)).findByUsername("testuser");
        verify(registrationMapper, times(1)).mapToDto(testUser);
    }

    /**
     * TEST 2: Vale username
     */
    @Test
    void login_WithInvalidUsername_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("wronguser"))
                .thenReturn(Optional.empty());

        loginRequest.setUsername("wronguser");

        // ========== ACT & ASSERT ==========
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.login(loginRequest),
                "Peaks viskama InvalidCredentialsException kui kasutajat ei leita"
        );

        assertEquals("Invalid username or password", exception.getMessage());

        verify(usersRepository, times(1)).findByUsername("wronguser");
        verify(registrationMapper, never()).mapToDto(any());
    }

    /**
     * TEST 3: Vale password
     */
    @Test
    void login_WithInvalidPassword_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        loginRequest.setPassword("wrongpassword");

        // ========== ACT & ASSERT ==========
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.login(loginRequest),
                "Peaks viskama InvalidCredentialsException vale password'i korral"
        );

        assertEquals("Invalid username or password", exception.getMessage());

        verify(usersRepository, times(1)).findByUsername("testuser");
        verify(registrationMapper, never()).mapToDto(any());
    }

    /**
     * TEST 4: Null username
     */
    @Test
    void login_WithNullUsername_ThrowsException() {
        // ========== ARRANGE ==========
        loginRequest.setUsername(null);

        // ========== ACT & ASSERT ==========
        assertThrows(
                Exception.class,
                () -> loginService.login(loginRequest),
                "Peaks viskama exception kui username on null"
        );
    }

    /**
     * TEST 5: Tühja password kontroll
     */
    @Test
    void login_WithEmptyPassword_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        loginRequest.setPassword("");

        // ========== ACT & ASSERT ==========
        assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.login(loginRequest)
        );
    }

    /**
     * TEST 6: Case sensitivity test
     */
    @Test
    void login_UsernameIsCaseSensitive() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("TESTUSER"))
                .thenReturn(Optional.empty());

        loginRequest.setUsername("TESTUSER");

        // ========== ACT & ASSERT ==========
        assertThrows(
                InvalidCredentialsException.class,
                () -> loginService.login(loginRequest),
                "Username peaks olema case-sensitive"
        );

        verify(usersRepository, times(1)).findByUsername("TESTUSER");
    }

    /**
     * TEST 7: Repository tagastab kasutaja, aga mapper ebaõnnestub
     */
    @Test
    void login_WhenMapperFails_ThrowsException() {
        // ========== ARRANGE ==========
        when(usersRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(testUser));

        when(registrationMapper.mapToDto(testUser))
                .thenThrow(new RuntimeException("Mapping failed"));

        // ========== ACT & ASSERT ==========
        assertThrows(
                RuntimeException.class,
                () -> loginService.login(loginRequest)
        );

        verify(usersRepository, times(1)).findByUsername("testuser");
        verify(registrationMapper, times(1)).mapToDto(testUser);
    }
}