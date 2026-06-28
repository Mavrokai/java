package com.myrestaurant.repository;

import com.myrestaurant.domain.RestaurantConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantConfigRepository extends JpaRepository<RestaurantConfig, Long> {

    Optional<RestaurantConfig> findFirstByConfiguredTrue();

    Optional<RestaurantConfig> findFirstByOrderByIdAsc();
}
