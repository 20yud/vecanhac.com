package com.vecanhac.ddd.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads/qrcodes");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/qrcodes/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}