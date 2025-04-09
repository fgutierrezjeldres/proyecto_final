package com.talento_futuro.proyecto_final.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.SensorData;

public interface ISensorDataRepository extends IGenericRepository<SensorData, Integer>{


    @Query("SELECT s FROM SensorData s " +
    "WHERE s.sensor.location.company = :company " +
    "AND s.receivedAt BETWEEN :from AND :to " +
    "AND s.sensor.id IN :sensorIds")
    List<SensorData> findSensorDataByFilters(
            @Param("company") Company company,
            @Param("from") Long from,
            @Param("to") Long to,
            @Param("sensorIds") List<Integer> sensorIds);

}
