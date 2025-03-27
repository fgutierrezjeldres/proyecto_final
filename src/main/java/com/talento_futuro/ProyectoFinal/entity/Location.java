package com.talento_futuro.ProyectoFinal.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "location")

public class Location {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "nombre_locacion")
	private String locationName;
	
	@Column(name = "nombre_pais")
	private String locationCountry;
	
	@Column(name = "nombre_ciudad")
	private String locationCity;
	
	@Column(name = "nombre_meta")
	private String locationMeta;

	@ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

	@OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors;
	
}
