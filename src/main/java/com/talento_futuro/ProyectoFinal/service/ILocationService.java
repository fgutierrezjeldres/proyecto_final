package com.talento_futuro.ProyectoFinal.service;

import com.talento_futuro.ProyectoFinal.dto.LocationDTO;
import com.talento_futuro.ProyectoFinal.entity.Location;

public interface ILocationService extends ICRUDService<Location, Integer>{

    LocationDTO registerLocation(LocationDTO locationDTO);
    LocationDTO updateLocation(LocationDTO locationDTO, Integer id);

}
