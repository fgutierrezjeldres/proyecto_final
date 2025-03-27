package com.talento_futuro.ProyectoFinal.repository;

import java.util.Optional;

import com.talento_futuro.ProyectoFinal.entity.Admin;

public interface IAdminRepository extends IGenericRepository<Admin, Integer>{

    Optional<Admin> findByUsername(String username);

}
