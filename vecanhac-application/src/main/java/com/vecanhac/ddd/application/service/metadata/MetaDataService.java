package com.vecanhac.ddd.application.service.metadata;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.application.dto.location.LocationDTO;

import java.util.List;

public interface MetaDataService {
    List<CategoryDTO> getAllCategories();
    List<LocationDTO> getAllLocations();
}