package com.talento_futuro.proyecto_final.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.talento_futuro.proyecto_final.dto.AdminDTO;
import com.talento_futuro.proyecto_final.entity.Admin;
import com.talento_futuro.proyecto_final.mapper.AdminMapper;
import com.talento_futuro.proyecto_final.repository.IAdminRepository;
import com.talento_futuro.proyecto_final.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private IAdminRepository adminRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    

    @Test
    void shouldRegisterAdminSuccessfully() {
        AdminDTO dto = new AdminDTO();
        dto.setUsername("admin");
        dto.setPassword("123");

        Admin adminEntity = new Admin();
        adminEntity.setPassword("123");

        Admin savedAdmin = new Admin();
        savedAdmin.setId(1);
        savedAdmin.setUsername("admin");

        AdminDTO resultDTO = new AdminDTO();
        resultDTO.setId(1);
        resultDTO.setUsername("admin");

        when(adminMapper.toEntity(dto)).thenReturn(adminEntity);
        when(passwordEncoder.encode("123")).thenReturn("123");
        when(adminRepository.save(adminEntity)).thenReturn(savedAdmin);
        when(adminMapper.toDTO(savedAdmin)).thenReturn(resultDTO);

        AdminDTO result = adminService.registerAdmin(dto);

        assertEquals("admin", result.getUsername());
        verify(passwordEncoder).encode("123");
        verify(adminRepository).save(adminEntity);
    }

    @Test
    void shouldLoginSuccessfully() {
        String username = "admin";
        String rawPassword = "123";
        String encodedPassword = "encrypted";

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(encodedPassword);

        when(adminRepository.findByUsername(username)).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = adminService.login(username, rawPassword);

        assertTrue(result);
        verify(adminRepository).findByUsername(username);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(adminRepository.findByUsername("no_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            adminService.login("no_user", "pass");
        });
    }

}
