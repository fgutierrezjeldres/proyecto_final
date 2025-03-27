package com.talento_futuro.ProyectoFinal.controller;

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

import com.talento_futuro.ProyectoFinal.dto.AdminDTO;
import com.talento_futuro.ProyectoFinal.dto.mapper.AdminMapper;
import com.talento_futuro.ProyectoFinal.entity.Admin;
import com.talento_futuro.ProyectoFinal.service.IAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;
    private final AdminMapper adminMapper;
    
    @PostMapping
    public ResponseEntity<AdminDTO> register(@RequestBody AdminDTO adminDTO) {
        AdminDTO savedAdminDTO = adminService.registerAdmin(adminDTO);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "created")
                            .buildAndExpand(savedAdminDTO.getId())    
                            .toUri();
        return ResponseEntity.created(location).body(savedAdminDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Integer id) {
        Admin admin = adminService.findById(id);
        AdminDTO adminDTO = adminMapper.toDTO(admin);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "fetched")
                            .buildAndExpand(id)
                            .toUri();
        return ResponseEntity.ok()
                             .location(location)
                             .body(adminDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Integer id, @RequestBody AdminDTO adminDTO) {
        Admin admin = adminMapper.toEntity(adminDTO);
        Admin updatedAdmin = adminService.update(admin, id);
        AdminDTO updatedAdminDTO = adminMapper.toDTO(updatedAdmin);

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .queryParam("status", "updated")
                            .buildAndExpand(id)
                            .toUri();

        return ResponseEntity.ok()
                             .location(location)
                             .body(updatedAdminDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        adminService.delete(id);
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
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<Admin> admins = adminService.findAll();
        List<AdminDTO> adminDTOs = admins.stream()
                                         .map(adminMapper::toDTO)
                                         .collect(Collectors.toList());

        URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/all")
                            .queryParam("status", "fetched")
                            .build()
                            .toUri();

        return ResponseEntity.ok()
                             .location(location)
                             .body(adminDTOs);
    }
}



