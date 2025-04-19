package com.talento_futuro.proyecto_final.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorDTO {
	
	private Integer id;
	@NotBlank(message = "El nombre del sensor no puede estar vacío")
	private String sensorName;

	@NotBlank(message = "La categoría del sensor no puede estar vacía")
	private String sensorCategory;

	@NotBlank(message = "Los metadatos del sensor no pueden estar vacíos")
	private String sensorMeta;

	@NotBlank(message = "La última vez en línea no puede estar vacía")
	private String lastOnline;

	@NotBlank(message = "El estado no puede estar vacío")
	private String status;

	@NotBlank(message = "El protocolo de comunicación no puede estar vacío")
	private String communicationProtocol;

	@NotNull(message = "El ID de ubicación no puede ser nulo")
	private Integer locationId;

	private List<SensorDataDTO> sensorData;

	@NotBlank(message = "La clave API del sensor no puede estar vacía")
	private String sensorApiKey;

}
