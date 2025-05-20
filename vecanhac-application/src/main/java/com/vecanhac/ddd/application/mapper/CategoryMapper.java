package com.vecanhac.ddd.application.mapper;

import com.vecanhac.ddd.application.dto.category.CategoryDTO;
import com.vecanhac.ddd.domain.category.CategoryEntity;

public class CategoryMapper {
    public static CategoryDTO toDTO(CategoryEntity entity) {
        return new CategoryDTO(entity.getId(), entity.getName(), entity.getSlug());
    }

    public static CategoryEntity toEntity(CategoryDTO dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        return entity;
    }
}