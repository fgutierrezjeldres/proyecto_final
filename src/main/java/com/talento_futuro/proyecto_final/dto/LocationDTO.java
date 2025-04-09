package com.talento_futuro.proyecto_final.dto;

import lombok.Data;

@Data
public class LocationDTO {
	
	private Integer id;
	private String locationName;
	private String locationCountry;
	private String locationCity;
	private String locationMeta;
	private Integer companyId;

}
