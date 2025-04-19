package com.talento_futuro.proyecto_final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompanyDTO {

	private Integer id;

	@NotBlank(message = "Company name cannot be empty")
	private String companyName;

	@NotNull(message = "Administrator ID cannot be null")
	private Integer adminId;

}
