package com.myrestaurant;

import com.myrestaurant.repository.DishRepository;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.ReviewRepository;
import com.myrestaurant.service.DataResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifie la suppression du contenu. Annotee @DirtiesContext pour reconstruire
 * le contexte (et re-seeder la base) apres la classe, sans polluer les autres tests.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AdminDataResetTest {

    @Autowired
    private DataResetService dataResetService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void clearContentRemovesEverything() {
        // Le seed initial a cree des plats et des menus.
        assertThat(dishRepository.count()).isGreaterThan(0);

        dataResetService.clearContent();

        assertThat(menuRepository.count()).isZero();
        assertThat(dishRepository.count()).isZero();
        assertThat(reviewRepository.count()).isZero();
    }
}
