package com.vecanhac.ddd.application.service.metadata.impl;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.application.dto.location.LocationDTO;
import com.vecanhac.ddd.application.service.metadata.MetaDataService;
import com.vecanhac.ddd.domain.category.CategoryRepository;
import com.vecanhac.ddd.domain.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataServiceImpl implements MetaDataService {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired private LocationRepository locationRepo;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(c -> {
                    CategoryDTO dto = new CategoryDTO();
                    dto.setId(c.getId());
                    dto.setName(c.getName());
                    dto.setSlug(c.getSlug());
                    return dto;
                })
                .toList();
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        return locationRepo.findAll()
                .stream()
                .map(l -> {
                    LocationDTO dto = new LocationDTO();
                    dto.setId(l.getId());
                    dto.setCity(l.getCity());
                    return dto;
                })
                .toList();
    }
}