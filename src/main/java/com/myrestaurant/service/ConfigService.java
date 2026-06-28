package com.myrestaurant.service;

import com.myrestaurant.domain.RestaurantConfig;
import com.myrestaurant.repository.RestaurantConfigRepository;
import com.myrestaurant.web.form.SetupForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

/**
 * Gestion de la configuration singleton du site.
 */
@Service
public class ConfigService {

    private final RestaurantConfigRepository repository;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

    public ConfigService(RestaurantConfigRepository repository,
                         StorageService storageService,
                         PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean isConfigured() {
        return repository.findFirstByConfiguredTrue().isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<RestaurantConfig> getConfig() {
        return repository.findFirstByConfiguredTrue();
    }

    public RestaurantConfig requireConfig() {
        return getConfig().orElseThrow(() -> new IllegalStateException("Site non configure"));
    }

    @Transactional
    public RestaurantConfig saveSetup(SetupForm form, MultipartFile banner) {
        RestaurantConfig config = repository.findFirstByOrderByIdAsc().orElseGet(RestaurantConfig::new);
        config.setName(form.getName());
        config.setUrl(form.getUrl());
        config.setThemeColor(form.getThemeColor());
        config.setAddress(form.getAddress());
        config.setPhone(form.getPhone());
        config.setEmail(form.getEmail());
        config.setPracticalInfo(form.getPracticalInfo());
        config.setAdminPasswordHash(passwordEncoder.encode(form.getAdminPassword()));

        String stored = storageService.store(banner);
        if (stored != null) {
            config.setBannerImage(stored);
        }

        config.setConfigured(true);
        return repository.save(config);
    }
}
