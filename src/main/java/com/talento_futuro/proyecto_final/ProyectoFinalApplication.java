package com.talento_futuro.proyecto_final;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ProyectoFinalApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry -> {
			if (entry.getKey().startsWith("APP_")) { // Filtra las variables que empiezan con "APP_"
				System.setProperty(entry.getKey(), entry.getValue());
				System.out.println("Loaded: " + entry.getKey() + "=" + entry.getValue());
			}
		});
		SpringApplication.run(ProyectoFinalApplication.class, args);
	}

}
