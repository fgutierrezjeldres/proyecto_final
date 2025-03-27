package com.talento_futuro.proyecto_final.service;

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.entity.Company;

public interface ICompanyService extends ICRUDService<Company, Integer>{

    CompanyDTO registerCompany(CompanyDTO companyDTO);
    Company updateCompany(CompanyDTO companyDTO, Integer id);

}
