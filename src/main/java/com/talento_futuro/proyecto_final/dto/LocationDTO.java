package com.talento_futuro.proyecto_final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDTO {

	private Integer id;

	@NotBlank(message = "El nombre de la ubicación no puede estar vacío")
	private String locationName;

	@NotBlank(message = "El país de la ubicación no puede estar vacío")
	private String locationCountry;

	@NotBlank(message = "La ciudad de la ubicación no puede estar vacía")
	private String locationCity;

	@NotBlank(message = "La metadata de la ubicación no puede estar vacía")
	private String locationMeta;

	@NotNull(message = "El ID de la compañía no puede ser nulo")
	private Integer companyId;

}
