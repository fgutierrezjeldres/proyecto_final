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

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.mapper.CompanyMapper;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.service.ICompanyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Endpoints para la gestión de compañías")
public class CompanyController {

        private final ICompanyService companyService;
        private final CompanyMapper companyMapper;

        @PostMapping
        @Operation(summary = "Registrar una compañía", description = "Crea una nueva compañía en el sistema.")
        @ApiResponse(responseCode = "201", description = "Compañía creada exitosamente")
        public ResponseEntity<EntityModel<CompanyDTO>> register(@Valid @RequestBody CompanyDTO companyDTO) {
                CompanyDTO savedCompanyDTO = companyService.registerCompany(companyDTO);

                EntityModel<CompanyDTO> resource = EntityModel.of(savedCompanyDTO,
                                linkTo(methodOn(CompanyController.class).getCompanyById(savedCompanyDTO.getId()))
                                                .withSelfRel(),
                                linkTo(methodOn(CompanyController.class).updateCompany(savedCompanyDTO.getId(), null))
                                                .withRel("update"),
                                linkTo(methodOn(CompanyController.class).deleteCompany(savedCompanyDTO.getId()))
                                                .withRel("delete"));

                URI location = linkTo(methodOn(CompanyController.class).getCompanyById(savedCompanyDTO.getId()))
                                .toUri();

                return ResponseEntity.created(location).body(resource);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener una compañía por ID", description = "Devuelve la información de una compañía específica.")
        @ApiResponse(responseCode = "200", description = "Compañía encontrada")
        @ApiResponse(responseCode = "404", description = "Compañía no encontrada")
        public ResponseEntity<EntityModel<CompanyDTO>> getCompanyById(@PathVariable Integer id) {
                Company company = companyService.findById(id);
                CompanyDTO companyDTO = companyMapper.toDTO(company);

                EntityModel<CompanyDTO> resource = EntityModel.of(companyDTO,
                                linkTo(methodOn(CompanyController.class).getCompanyById(id)).withSelfRel(),
                                linkTo(methodOn(CompanyController.class).updateCompany(id, null)).withRel("update"),
                                linkTo(methodOn(CompanyController.class).deleteCompany(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar una compañía", description = "Modifica los datos de una compañía existente.")
        @ApiResponse(responseCode = "200", description = "Compañía actualizada exitosamente")
        public ResponseEntity<EntityModel<CompanyDTO>> updateCompany(@PathVariable Integer id,
                        @RequestBody CompanyDTO companyDTO) {
                CompanyDTO updatedCompanyDTO = companyService.updateCompany(companyDTO, id);
                EntityModel<CompanyDTO> resource = EntityModel.of(updatedCompanyDTO,
                                linkTo(methodOn(CompanyController.class).getCompanyById(id)).withSelfRel(),
                                linkTo(methodOn(CompanyController.class).deleteCompany(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar una compañía", description = "Elimina una compañía por su ID.")
        @ApiResponse(responseCode = "204", description = "Compañía eliminada exitosamente")
        public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
                companyService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping
        @Operation(summary = "Listar todas las compañías", description = "Obtiene una lista de todas las compañías registradas.")
        @ApiResponse(responseCode = "200", description = "Lista de compañías obtenida exitosamente")
        public ResponseEntity<CollectionModel<EntityModel<CompanyDTO>>> getAllCompanies() {
                List<Company> companies = companyService.findAll();
                List<EntityModel<CompanyDTO>> companyResources = companies.stream()
                                .map(company -> {
                                        CompanyDTO dto = companyMapper.toDTO(company);
                                        EntityModel<CompanyDTO> model = EntityModel.of(dto);
                                        model.add(linkTo(methodOn(CompanyController.class).getCompanyById(dto.getId()))
                                                        .withSelfRel());
                                        return model;
                                })
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<CompanyDTO>> collectionModel = CollectionModel.of(companyResources,
                                linkTo(
                                                methodOn(CompanyController.class).getAllCompanies())
                                                .withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }
}