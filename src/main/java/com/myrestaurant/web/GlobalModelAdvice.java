package com.myrestaurant.web;

import com.myrestaurant.domain.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injecte la configuration du restaurant (nom, theme, banniere, contact)
 * dans le modele de toutes les vues afin de personnaliser header et titre.
 */
@ControllerAdvice(basePackages = "com.myrestaurant.web")
public class GlobalModelAdvice {

    private final ConfigService configService;

    public GlobalModelAdvice(ConfigService configService) {
        this.configService = configService;
    }

    @ModelAttribute("config")
    public RestaurantConfig config() {
        return configService.getConfig().orElse(null);
    }
}
