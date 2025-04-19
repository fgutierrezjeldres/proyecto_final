package com.talento_futuro.proyecto_final.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.talento_futuro.proyecto_final.dto.SensorDataDTO;
import com.talento_futuro.proyecto_final.mapper.SensorDataMapper;
import com.talento_futuro.proyecto_final.entity.SensorData;
import com.talento_futuro.proyecto_final.service.ISensorDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/sensor_data")
@RequiredArgsConstructor
@Tag(name = "Sensor Data", description = "Endpoints para registrar, consultar y administrar datos de sensores")
public class SensorDataController {

        private final ISensorDataService sensorDataService;
        private final SensorDataMapper sensorDataMapper;

        @PostMapping
        @Operation(summary = "Insertar datos del sensor", description = "Recibe un JSON con múltiples registros de datos de sensores.")
        @ApiResponse(responseCode = "201", description = "Datos insertados exitosamente")
        public ResponseEntity<CollectionModel<EntityModel<SensorDataDTO>>> insertSensorData(
                        @RequestBody JsonNode data) {
                List<SensorDataDTO> savedSensorData = sensorDataService.registerSensorData(data);

                List<EntityModel<SensorDataDTO>> resources = savedSensorData.stream()
                                .map(dto -> EntityModel.of(dto,
                                                linkTo(methodOn(SensorDataController.class)
                                                                .getSensorDataById(dto.getId())).withSelfRel(),
                                                linkTo(methodOn(SensorDataController.class)
                                                                .updateSensorData(dto.getId(), null)).withRel("update"),
                                                linkTo(methodOn(SensorDataController.class)
                                                                .deleteSensorData(dto.getId())).withRel("delete")))
                                .collect(Collectors.toList());

                URI location = linkTo(
                                methodOn(SensorDataController.class).getSensorDataById(savedSensorData.get(0).getId()))
                                .toUri();

                return ResponseEntity.created(location).body(CollectionModel.of(resources));
        }

        @GetMapping("/{id}")
        @Operation(summary = "Obtener dato de sensor por ID", description = "Devuelve un único dato de sensor basado en su ID.")
        @ApiResponse(responseCode = "200", description = "Dato encontrado exitosamente")
        @ApiResponse(responseCode = "404", description = "Dato no encontrado")
        public ResponseEntity<EntityModel<SensorDataDTO>> getSensorDataById(@Valid @PathVariable Integer id) {
                SensorData sensorData = sensorDataService.findById(id);
                SensorDataDTO dto = sensorDataMapper.toDTO(sensorData);

                EntityModel<SensorDataDTO> resource = EntityModel.of(dto,
                                linkTo(methodOn(SensorDataController.class).getSensorDataById(id)).withSelfRel(),
                                linkTo(methodOn(SensorDataController.class).updateSensorData(id, null))
                                                .withRel("update"),
                                linkTo(methodOn(SensorDataController.class).deleteSensorData(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Actualizar dato del sensor", description = "Modifica un dato del sensor por ID.")
        @ApiResponse(responseCode = "200", description = "Dato actualizado exitosamente")
        public ResponseEntity<EntityModel<SensorDataDTO>> updateSensorData(@PathVariable Integer id,
                        @RequestBody SensorDataDTO dto) {
                SensorDataDTO updatedDTO = sensorDataService.updateSensorData(dto, id);
                EntityModel<SensorDataDTO> resource = EntityModel.of(updatedDTO,
                                linkTo(methodOn(SensorDataController.class).getSensorDataById(id)).withSelfRel(),
                                linkTo(methodOn(SensorDataController.class).deleteSensorData(id)).withRel("delete"));

                return ResponseEntity.ok(resource);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Eliminar dato del sensor", description = "Elimina un dato específico del sensor.")
        @ApiResponse(responseCode = "204", description = "Dato eliminado exitosamente")
        public ResponseEntity<Void> deleteSensorData(@PathVariable Integer id) {
                sensorDataService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Listar todos los datos de sensores", description = "Obtiene todos los registros de datos almacenados.")
        @ApiResponse(responseCode = "200", description = "Datos obtenidos exitosamente")
        public ResponseEntity<CollectionModel<EntityModel<SensorDataDTO>>> getAllSensorData() {
                List<SensorData> sensorDataList = sensorDataService.findAll();
                List<EntityModel<SensorDataDTO>> sensorDataDTOsWithLinks = sensorDataList.stream()
                                .map(sensorData -> {
                                        SensorDataDTO sensorDataDTO = sensorDataMapper.toDTO(sensorData);
                                        EntityModel<SensorDataDTO> entityModel = EntityModel.of(sensorDataDTO);
                                        entityModel.add(linkTo(methodOn(SensorDataController.class)
                                                                        .getSensorDataById(sensorDataDTO.getId()))
                                                        .withSelfRel());

                                        return entityModel;
                                })
                                .collect(Collectors.toList());
                CollectionModel<EntityModel<SensorDataDTO>> collectionModel = CollectionModel
                                .of(sensorDataDTOsWithLinks);
                collectionModel.add(linkTo(SensorDataController.class)
                                .withSelfRel());

                URI uri = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/all")
                                .queryParam("status", "fetched")
                                .build()
                                .toUri();

                return ResponseEntity.ok()
                                .location(uri)
                                .body(collectionModel);
        }

        @GetMapping("/all")
        @Operation(summary = "Filtrar datos de sensores", description = "Filtra los datos por rango de tiempo y lista de sensores. La API key de la compañía puede enviarse por header o query param.")
        @ApiResponse(responseCode = "200", description = "Datos filtrados correctamente")
        @ApiResponse(responseCode = "401", description = "API Key no proporcionada")
        public ResponseEntity<CollectionModel<EntityModel<SensorDataDTO>>> getFilteredSensorData(
                        @RequestHeader(value = "company_api_key", required = false) @Parameter(description = "API Key de la compañía (en header o query param)") String companyApiKeyHeader,
                        @RequestParam(value = "company_api_key", required = false) String companyApiKeyParam,
                        @RequestParam(value = "from") @Parameter(description = "Timestamp inicial en formato epoch") Long fromTimestamp,
                        @RequestParam(value = "to") @Parameter(description = "Timestamp final en formato epoch") Long toTimestamp,
                        @RequestParam(value = "sensor_id") @Parameter(description = "Lista de IDs de sensores") List<Integer> sensorIds) {

                String companyApiKey = (companyApiKeyHeader != null) ? companyApiKeyHeader : companyApiKeyParam;
                if (companyApiKey == null || companyApiKey.isBlank()) {
                        CollectionModel<EntityModel<SensorDataDTO>> emptyCollection = CollectionModel
                                        .of(Collections.emptyList());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyCollection);
                }

                List<SensorData> sensorDataList = sensorDataService.findFilteredData(companyApiKey, fromTimestamp,
                                toTimestamp, sensorIds);
                List<SensorDataDTO> sensorDataDTOs = sensorDataList.stream()
                                .map(sensorDataMapper::toDTO)
                                .collect(Collectors.toList());

                List<EntityModel<SensorDataDTO>> entityModels = sensorDataDTOs.stream()
                                .map(sensorDataDTO -> {
                                        EntityModel<SensorDataDTO> entityModel = EntityModel.of(sensorDataDTO);
                                        entityModel.add(linkTo(methodOn(SensorDataController.class)
                                                                        .getSensorDataById(sensorDataDTO.getId()
                                                                                        .intValue()))
                                                        .withSelfRel());
                                        return entityModel;
                                })
                                .collect(Collectors.toList());
                CollectionModel<EntityModel<SensorDataDTO>> collectionModel = CollectionModel.of(entityModels);
                collectionModel.add(linkTo(SensorDataController.class).withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }
}
