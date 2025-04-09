package com.talento_futuro.proyecto_final.dto;

import java.util.List;
import lombok.Data;

@Data
public class SensorDTO {
	
	private Integer id;
	private String sensorName;
	private String sensorCategory;
	private String sensorMeta;
	private String lastOnline;
	private String status;
	private String communicationProtocol;
	private Integer locationId;
    private List<SensorDataDTO> sensorData;
	private String sensorApiKey; 

}
