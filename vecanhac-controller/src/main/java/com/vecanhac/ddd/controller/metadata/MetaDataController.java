package com.vecanhac.ddd.controller.metadata;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.application.dto.location.LocationDTO;
import com.vecanhac.ddd.application.service.metadata.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meta")
public class MetaDataController {

    @Autowired
    private MetaDataService metaDataService;

    @GetMapping("/categories")
    public List<CategoryDTO> getCategories() {
        return metaDataService.getAllCategories();
    }

    @GetMapping("/locations")
    public List<LocationDTO> getLocations() {
        return metaDataService.getAllLocations();
    }
}