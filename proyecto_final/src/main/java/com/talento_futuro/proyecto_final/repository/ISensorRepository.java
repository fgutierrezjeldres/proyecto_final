package com.talento_futuro.proyecto_final.repository;

import java.util.Optional;

import com.talento_futuro.proyecto_final.entity.Sensor;

public interface ISensorRepository extends IGenericRepository<Sensor, Integer>{

    boolean existsBySensorApiKey(String sensorApiKey);
    Optional<Sensor> findBySensorName(String sensorName);
    Optional<Sensor> findBySensorApiKey(String sensorApiKey);

}
