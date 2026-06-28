package com.myrestaurant;

import com.myrestaurant.repository.DishRepository;
import com.myrestaurant.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifie que le contexte Spring se charge entierement (entites, beans,
 * securite, Thymeleaf) et que le seed de demonstration s'execute.
 */
@SpringBootTest
@ActiveProfiles("test")
class ContextLoadTest {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void contextLoadsAndSeedRuns() {
        assertThat(dishRepository.count()).isGreaterThan(0);
        assertThat(menuRepository.findFirstByCarteTrue()).isPresent();
        assertThat(menuRepository.findByCarteFalseOrderByPositionAsc()).isNotEmpty();
    }
}
