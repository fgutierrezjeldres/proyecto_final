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

import com.talento_futuro.proyecto_final.dto.AdminDTO;
import com.talento_futuro.proyecto_final.mapper.AdminMapper;
import com.talento_futuro.proyecto_final.entity.Admin;
import com.talento_futuro.proyecto_final.service.IAdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

@RestController
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@Tag(name = "Admins", description = "Endpoints para la gestión de admins")
public class AdminController {

        private final IAdminService adminService;
        private final AdminMapper adminMapper;

        @PostMapping
        @Operation(summary = "Registrar un admin", description = "Crea un nuevo administrador en el sistema.")
        @ApiResponse(responseCode = "201", description = "admin creado exitosamente")
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
        @Operation(summary = "Obtener un admin por ID", description = "Devuelve la información de un administrador específico.")
        @ApiResponse(responseCode = "200", description = "Admin encontrado")
        @ApiResponse(responseCode = "404", description = "Admin no encontrado")
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
        @Operation(summary = "Actualizar un admin", description = "Modifica los datos de un administrador existente.")
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
        @Operation(summary = "Eliminar un admin", description = "Elimina un administrador por su ID.")
        @ApiResponse(responseCode = "204", description = "Admin eliminado exitosamente")
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
        @Operation(summary = "Listar todos los admins", description = "Obtiene una lista de todos las administradores registrados.")
        public ResponseEntity<CollectionModel<EntityModel<AdminDTO>>> getAllAdmins() {
                List<Admin> admins = adminService.findAll();

                List<EntityModel<AdminDTO>> adminResources = admins.stream()
                                .map(admin -> {
                                        AdminDTO dto = adminMapper.toDTO(admin);
                                        EntityModel<AdminDTO> model = EntityModel.of(dto);
                                        model.add(WebMvcLinkBuilder.linkTo(
                                                        WebMvcLinkBuilder.methodOn(AdminController.class)
                                                                        .getAdminById(dto.getId()))
                                                        .withSelfRel());
                                        return model;
                                })
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<AdminDTO>> collectionModel = CollectionModel.of(adminResources,
                                WebMvcLinkBuilder.linkTo(
                                                WebMvcLinkBuilder.methodOn(AdminController.class).getAllAdmins())
                                                .withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }
}