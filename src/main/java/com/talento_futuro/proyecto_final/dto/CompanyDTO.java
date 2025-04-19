package com.talento_futuro.proyecto_final.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyDTO {
	
	private Integer id;
	@NotBlank(message = "El nombre de la compañía no puede estar vacío")
	private String companyName;

	@NotNull(message = "El ID del administrador no puede ser nulo")
	private Integer adminId;
	
}
 