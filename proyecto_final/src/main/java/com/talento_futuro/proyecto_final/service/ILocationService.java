package com.talento_futuro.proyecto_final.service;

import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.entity.Location;

public interface ILocationService extends ICRUDService<Location, Integer>{

    LocationDTO registerLocation(LocationDTO locationDTO);
    LocationDTO updateLocation(LocationDTO locationDTO, Integer id);

}
