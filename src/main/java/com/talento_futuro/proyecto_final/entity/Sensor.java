package com.talento_futuro.proyecto_final.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "sensor")

public class Sensor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "nombre_sensor")
	private String sensorName;
	
	@Column(name = "categoria_sensor")
	private String sensorCategory;
	
	@Column(name = "sensor_meta")
	private String sensorMeta;
	
	@Column(name = "sensor_llave")
	private String sensorApiKey;

	@Column(name = "ultima_conexion")
	private String lastOnline ;

	@Column(name = "status", nullable = false)
	private String status;

    @Column(name = "protocolo", nullable = false)
    private String communicationProtocol;

	@ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

	@OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorData> sensorData = new ArrayList<>();

}
