package com.talento_futuro.proyecto_final.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talento_futuro.proyecto_final.dto.AdminDTO;
import com.talento_futuro.proyecto_final.mapper.AdminMapper;
import com.talento_futuro.proyecto_final.entity.Admin;
import com.talento_futuro.proyecto_final.repository.IAdminRepository;
import com.talento_futuro.proyecto_final.repository.IGenericRepository;
import com.talento_futuro.proyecto_final.service.IAdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl extends CRUDServiceImpl<Admin, Integer> implements IAdminService {

    private final IAdminRepository repository;
    private final PasswordEncoder encoder;
    private final AdminMapper adminMapper;

    @Override
    protected IGenericRepository<Admin, Integer> getRepository() {
        return repository;
    }

    @Override
    @Transactional
    public AdminDTO registerAdmin(AdminDTO adminDTO) {
        Admin admin = adminMapper.toEntity(adminDTO);
        admin.setPassword(encoder.encode(admin.getPassword()));
        Admin savedAdmin = save(admin);
        return adminMapper.toDTO(savedAdmin);
    }

    public boolean login(String username, String password) {
        Admin admin = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return encoder.matches(password, admin.getPassword());
    }

    @Override
    public AdminDTO updateAdmin(Integer id, AdminDTO adminDTO) {
        Admin admin = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        admin.setPassword(adminDTO.getPassword());
        admin.setUsername(adminDTO.getUsername());
        Admin updatedAdmin = update(admin, id);
        return adminMapper.toDTO(updatedAdmin);
    }

}
