package com.talento_futuro.ProyectoFinal.service;

import com.talento_futuro.ProyectoFinal.dto.CompanyDTO;
import com.talento_futuro.ProyectoFinal.entity.Company;

public interface ICompanyService extends ICRUDService<Company, Integer>{

    CompanyDTO registerCompany(CompanyDTO companyDTO);
    Company updateCompany(CompanyDTO companyDTO, Integer id);

}
