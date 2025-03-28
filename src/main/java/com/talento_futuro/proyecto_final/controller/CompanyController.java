package com.talento_futuro.proyecto_final.controller;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.mapper.CompanyMapper;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.service.ICompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;
    private final CompanyMapper companyMapper;
    
    @PostMapping
    public ResponseEntity<CompanyDTO> register(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO savedCompanyDTO = companyService.registerCompany(companyDTO);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "created")
                            .buildAndExpand(savedCompanyDTO.getId())    
                            .toUri();
        return ResponseEntity.created(location).body(savedCompanyDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Integer id) {
        Company company = companyService.findById(id);
        CompanyDTO companyDTO = companyMapper.toDTO(company);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "fetched")
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.ok()
                             .location(location)
                             .body(companyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Integer id, @RequestBody CompanyDTO companyDTO) {

        Company updatedCompany = companyService.updateCompany(companyDTO, id);
        CompanyDTO updatedCompanyDTO = companyMapper.toDTO(updatedCompany);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "updated")
                            .buildAndExpand(id)
                            .toUri();

        return ResponseEntity.ok()
                            .location(location)
                            .body(updatedCompanyDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "deleted")
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.noContent()
                             .location(location)
                             .build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<Company> companies = companyService.findAll();
        List<CompanyDTO> companyDTOs = companies.stream()
                                                .map(companyMapper::toDTO)
                                                .collect(Collectors.toList());

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/all")
                            .queryParam("status", "fetched")
                            .build()
                            .toUri();

        return ResponseEntity.ok()
                             .location(location)
                             .body(companyDTOs);
    }
}
