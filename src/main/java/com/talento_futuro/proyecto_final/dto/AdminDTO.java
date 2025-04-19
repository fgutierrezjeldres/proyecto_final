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

	@NotBlank(message = "Username cannot be empty")
	private String username;

	@NotBlank(message = "Password cannot be empty")
	private String password;

}
