package com.talento_futuro.proyecto_final.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.entity.Admin;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.mapper.CompanyMapper;
import com.talento_futuro.proyecto_final.repository.IAdminRepository;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.service.impl.CompanyServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @Mock
    private ICompanyRepository repository;

    @Mock
    private IAdminRepository adminRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void shouldRegisterCompanySuccessfully() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAdminId(1);
        companyDTO.setCompanyName("test");

        Admin admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");

        Company company = new Company();
        company.setId(10);
        company.setCompanyName("test");
        company.setCompanyApiKey("d3ee2929-212b-4077-af84-694a0e69b8e1");
        company.setAdmin(admin);

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(companyMapper.toEntity(companyDTO, admin)).thenReturn(company);
        when(repository.save(any(Company.class))).thenReturn(company);
        when(companyMapper.toDTO(company)).thenReturn(companyDTO); 

        CompanyDTO result = companyService.registerCompany(companyDTO);
        assertNotNull(result);
        assertEquals("My Company", result.getCompanyName());
        assertEquals(1, result.getAdminId());
    }

    @Test
    void shouldUpdateCompanySuccessfully() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setAdminId(1);

        Admin admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");

        Company existingCompany = new Company();
        existingCompany.setId(1);
        existingCompany.setCompanyApiKey("old-api-key");

        Company updatedCompany = new Company();
        updatedCompany.setId(1);
        updatedCompany.setAdmin(admin);
        updatedCompany.setCompanyApiKey("new-api-key");

        when(repository.findById(1)).thenReturn(Optional.of(existingCompany));
        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(companyMapper.toEntity(companyDTO, admin)).thenReturn(updatedCompany);
        when(repository.save(any(Company.class))).thenReturn(updatedCompany);

        Company result = companyService.updateCompany(companyDTO, 1);

        assertEquals(1, result.getId());
        assertEquals("new-api-key", result.getCompanyApiKey());
        assertEquals("admin", result.getAdmin().getUsername());
    }
}
