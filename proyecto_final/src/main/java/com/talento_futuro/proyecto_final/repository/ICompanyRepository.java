package com.talento_futuro.proyecto_final.repository;

import java.util.Optional;

import com.talento_futuro.proyecto_final.entity.Company;

public interface ICompanyRepository extends IGenericRepository<Company, Integer>{

    Optional<Company> findByCompanyApiKey(String companyApiKey);

}
