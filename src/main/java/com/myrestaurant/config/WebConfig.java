package com.myrestaurant.config;

import com.myrestaurant.service.StorageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Expose le dossier d'upload en tant que ressources statiques sous /uploads/**.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StorageService storageService;

    public WebConfig(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + storageService.getRoot().toString() + "/");
    }
}
