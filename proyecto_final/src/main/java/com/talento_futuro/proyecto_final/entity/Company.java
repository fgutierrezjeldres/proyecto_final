package com.talento_futuro.proyecto_final.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
@Table(name = "company")

public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "nombre_compania")
	private String companyName;
	
	@Column(name = "llave_compania")
	private String companyApiKey;

	@ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;
	

	@PrePersist
    private void generateCompanyApiKey() {
        if (this.companyApiKey == null || this.companyApiKey.isEmpty()) {
            this.companyApiKey = UUID.randomUUID().toString();
			System.out.println("Generated Company API Key: " + this.companyApiKey);
        }else {
			System.out.println("Company API Key already set: " + this.companyApiKey);
		}
    }

}
