package com.talento_futuro.proyecto_final.service;

import com.talento_futuro.proyecto_final.dto.AdminDTO;
import com.talento_futuro.proyecto_final.entity.Admin;

public interface IAdminService extends ICRUDService<Admin, Integer>{
    AdminDTO  registerAdmin(AdminDTO adminDTO);
    boolean login(String username, String password);


}
