package com.talento_futuro.ProyectoFinal.dto;

import java.util.List;

import com.talento_futuro.ProyectoFinal.entity.enums.CommunicationProtocol;
import com.talento_futuro.ProyectoFinal.entity.enums.StatusSensor;

import lombok.Data;

@Data
public class SensorDTO {
	
	private Integer id;
	private String sensorName;
	private String sensorCategory;
	private String sensorMeta;
	private String lastOnline;
	private StatusSensor status;
	private CommunicationProtocol communicationProtocol;
	private Integer locationId;
    private List<SensorDataDTO> sensorData;
	private String sensorApiKey; 

}
