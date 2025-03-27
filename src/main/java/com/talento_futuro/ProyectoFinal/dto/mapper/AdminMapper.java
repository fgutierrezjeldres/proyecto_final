package com.talento_futuro.ProyectoFinal.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.talento_futuro.ProyectoFinal.dto.AdminDTO;
import com.talento_futuro.ProyectoFinal.entity.Admin;

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
