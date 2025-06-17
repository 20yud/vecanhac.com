package com.vecanhac.ddd.infrastructure.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // hoặc "/src/main/resources" nếu để file trong resource
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

        return new Cloudinary(cloudinaryUrl);
    }
}