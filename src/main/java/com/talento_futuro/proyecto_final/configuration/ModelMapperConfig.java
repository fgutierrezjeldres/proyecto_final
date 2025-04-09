package com.talento_futuro.proyecto_final.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.talento_futuro.proyecto_final.dto.CompanyDTO;
import com.talento_futuro.proyecto_final.dto.LocationDTO;
import com.talento_futuro.proyecto_final.dto.SensorDTO;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.entity.Company;
import com.talento_futuro.proyecto_final.entity.Location;
import com.talento_futuro.proyecto_final.entity.Sensor;
import com.talento_futuro.proyecto_final.entity.SensorData;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        companyMapper(mapper);
        locationMapper(mapper);
        sensorMapper(mapper);
        sensorDataMapper(mapper);
        return mapper;
    }

    private ModelMapper companyMapper(ModelMapper mapper) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        mapper.typeMap(Company.class, CompanyDTO.class)
            .addMapping(src -> src.getAdmin().getId(), CompanyDTO::setAdminId);

        mapper.typeMap(CompanyDTO.class, Company.class)
            .addMappings(m -> m.skip(Company::setAdmin))
            .addMappings(m -> m.skip(Company::setLocations));

        return mapper;
    }

    
    private ModelMapper locationMapper(ModelMapper mapper) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        mapper.typeMap(Location.class, LocationDTO.class)
            .addMappings(m -> m.map(
                src -> src.getCompany().getId(), 
                LocationDTO::setCompanyId 
            ));
        
        mapper.typeMap(LocationDTO.class, Location.class)
            .addMappings(m -> m.skip(Location::setCompany))
            .addMappings(m -> m.skip(Location::setSensors));

        return mapper;
    }

    
    private ModelMapper sensorMapper(ModelMapper mapper) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        mapper.typeMap(Sensor.class, SensorDTO.class)
            .addMapping(src -> src.getLocation().getId(), SensorDTO::setLocationId)
            .addMapping(src -> src.getSensorData(), SensorDTO::setSensorData);

        mapper.typeMap(SensorDTO.class, Sensor.class)
            .addMappings(m -> m.skip(Sensor::setLocation))
            .addMappings(m -> m.skip(Sensor::setSensorData));

        return mapper;
    }

    
    private ModelMapper sensorDataMapper(ModelMapper mapper) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        mapper.typeMap(SensorData.class, SensorDataDTO.class)
            .addMapping(src -> src.getSensor().getId(), SensorDataDTO::setSensorId);
        
        mapper.typeMap(SensorDataDTO.class, SensorData.class)
            .addMappings(m -> m.skip(SensorData::setSensor));

        return mapper;
    }
}
    

