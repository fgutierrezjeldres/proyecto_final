package com.talento_futuro.proyecto_final.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.proyecto_final.dto.AdminDTO;
import com.talento_futuro.proyecto_final.entity.Admin;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminMapper {

    private final ModelMapper modelMapper;

    public AdminDTO toDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }

    public Admin toEntity(AdminDTO adminDTO) {
        return modelMapper.map(adminDTO, Admin.class);
    }


}
