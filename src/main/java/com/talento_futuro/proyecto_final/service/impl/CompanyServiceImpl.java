package com.talento_futuro.proyecto_final.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.mapper.CompanyMapper;
import com.talento_futuro.proyecto_final.entity.Admin;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.exception.ModelNotFoundException;
import com.talento_futuro.proyecto_final.repository.IAdminRepository;
import com.talento_futuro.proyecto_final.repository.ICompanyRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl extends CRUDServiceImpl<Company, Integer> implements ICompanyService {

    private final ICompanyRepository repository;
    private final CompanyMapper companyMapper;
    private final IAdminRepository adminRepository;

    @Override
    protected IGenericRepository<Company, Integer> getRepository() {
        return repository;
        
    }

    @Override
    @Transactional
    public CompanyDTO registerCompany(CompanyDTO companyDTO) {

        Admin admin = adminRepository.findById(companyDTO.getAdminId())
                .orElseThrow(() -> new ModelNotFoundException("Admin not found"));
        
        Company company = companyMapper.toEntity(companyDTO, admin);
        Company  savedCompany = save(company);
        return companyMapper.toDTO(savedCompany);
    }

    @Override
    @Transactional
    public Company updateCompany(CompanyDTO companyDTO, Integer id) {
        Company existingCompany = repository.findById(id)
        .orElseThrow(() -> new ModelNotFoundException("Company not found"));
        Admin admin = adminRepository.findById(companyDTO.getAdminId())
                .orElseThrow(() -> new ModelNotFoundException("Admin not found"));
        Company updatedCompany = companyMapper.toEntity(companyDTO, admin);
        updatedCompany.setId(existingCompany.getId());
        updatedCompany.setCompanyApiKey(existingCompany.getCompanyApiKey());

        return repository.save(updatedCompany);
    }

}
