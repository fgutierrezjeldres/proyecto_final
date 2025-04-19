package com.talento_futuro.proyecto_final.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
	
	private Integer id;
	
	@NotBlank(message = "El nombre de usuario no puede estar vacío")
	private String username;

	@NotBlank(message = "La contraseña no puede estar vacía")
	private String password;

}
