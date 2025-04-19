package com.talento_futuro.proyecto_final.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SensorDTO {

	private Integer id;

	@NotBlank(message = "Sensor name cannot be empty")
	private String sensorName;

	@NotBlank(message = "Sensor category cannot be empty")
	private String sensorCategory;

	@NotBlank(message = "Sensor metadata cannot be empty")
	private String sensorMeta;

	@NotBlank(message = "Last online cannot be empty")
	private String lastOnline;

	@NotBlank(message = "Status cannot be empty")
	private String status;

	@NotBlank(message = "Communication protocol cannot be empty")
	private String communicationProtocol;

	@NotNull(message = "Location ID cannot be null")
	private Integer locationId;

	private List<SensorDataDTO> sensorData;

	private String sensorApiKey;

	@NotBlank(message = "Company API key cannot be empty")
	private String companyApiKey;

}
