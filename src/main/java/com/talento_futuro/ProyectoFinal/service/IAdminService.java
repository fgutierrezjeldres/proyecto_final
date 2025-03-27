package com.talento_futuro.ProyectoFinal.service;

import com.talento_futuro.ProyectoFinal.dto.AdminDTO;
import com.talento_futuro.ProyectoFinal.entity.Admin;

public interface IAdminService extends ICRUDService<Admin, Integer>{
    AdminDTO  registerAdmin(AdminDTO adminDTO);
    boolean login(String username, String password);


}
