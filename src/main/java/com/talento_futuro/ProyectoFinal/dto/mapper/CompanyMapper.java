package com.talento_futuro.ProyectoFinal.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.ProyectoFinal.dto.CompanyDTO;
import com.talento_futuro.ProyectoFinal.entity.Admin;
import com.talento_futuro.ProyectoFinal.entity.Company;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    private final ModelMapper modelMapper;

    public CompanyDTO toDTO(Company company) {
        CompanyDTO dto = modelMapper.map(company, CompanyDTO.class);
        dto.setAdminId(company.getAdmin().getId());
        return dto;
    }

    public Company toEntity(CompanyDTO companyDTO, Admin admin ) {
        Company company = modelMapper.map(companyDTO, Company.class);
        company.setAdmin(admin); // Recibe el admin desde el Service
        return company;
    }

}
