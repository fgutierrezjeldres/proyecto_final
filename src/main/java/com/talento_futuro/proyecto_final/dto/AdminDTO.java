package com.talento_futuro.proyecto_final.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
	
	private Integer id;
	private String username;
	
	@JsonIgnore
	private String password;

}
