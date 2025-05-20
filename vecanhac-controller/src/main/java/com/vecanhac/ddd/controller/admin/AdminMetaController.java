package com.vecanhac.ddd.controller.admin;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.application.dto.location.LocationDTO;
import com.vecanhac.ddd.application.service.admin.AdminAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/meta")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminMetaController {

    private final AdminAppService metaService;

    @GetMapping("/categories")
    public List<CategoryDTO> getCategories() {
        return metaService.getAllCategories();
    }

    @PostMapping("/categories")
    public CategoryDTO createCategory(@RequestBody CategoryDTO dto) {
        return metaService.createCategory(dto);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        metaService.deleteCategory(id);
    }

    @GetMapping("/locations")
    public List<LocationDTO> getLocations() {
        return metaService.getAllLocations();
    }

    @PostMapping("/locations")
    public LocationDTO createLocation(@RequestBody LocationDTO dto) {
        return metaService.createLocation(dto);
    }

    @DeleteMapping("/locations/{id}")
    public void deleteLocation(@PathVariable Long id) {
        metaService.deleteLocation(id);
    }
}
