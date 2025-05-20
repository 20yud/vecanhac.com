package com.vecanhac.ddd.application.mapper;

import com.vecanhac.ddd.application.dto.location.LocationDTO;
import com.vecanhac.ddd.domain.location.LocationEntity;


public class LocationMapper {
    public static LocationDTO toDTO(LocationEntity entity) {
        return new LocationDTO(entity.getId(), entity.getCity(), entity.getPhoneCode());
    }

    public static LocationEntity toEntity(LocationDTO dto) {
        LocationEntity entity = new LocationEntity();
        entity.setCity(dto.getCity());
        entity.setPhoneCode(dto.getPhoneCode());
        return entity;
    }
}
