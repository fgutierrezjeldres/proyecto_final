package com.talento_futuro.proyecto_final.repository;

import java.util.Optional;

import com.talento_futuro.proyecto_final.entity.Admin;

public interface IAdminRepository extends IGenericRepository<Admin, Integer>{

    Optional<Admin> findByUsername(String username);

}
