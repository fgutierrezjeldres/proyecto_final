package com.talento_futuro.proyecto_final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDTO {

	private Integer id;

	@NotBlank(message = "Location name cannot be empty")
	private String locationName;

	@NotBlank(message = "Location country cannot be empty")
	private String locationCountry;

	@NotBlank(message = "Location city cannot be empty")
	private String locationCity;

	@NotBlank(message = "Location metadata cannot be empty")
	private String locationMeta;

	@NotNull(message = "Company ID cannot be null")
	private Integer companyId;

}
