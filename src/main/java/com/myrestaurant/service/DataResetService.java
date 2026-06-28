package com.myrestaurant.service;

import com.myrestaurant.repository.DishRepository;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.RestaurantConfigRepository;
import com.myrestaurant.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Operations de maintenance : suppression du contenu ou reinitialisation
 * complete du site. Reserve a l'administrateur.
 */
@Service
public class DataResetService {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantConfigRepository configRepository;

    public DataResetService(ReviewRepository reviewRepository,
                            MenuRepository menuRepository,
                            DishRepository dishRepository,
                            RestaurantConfigRepository configRepository) {
        this.reviewRepository = reviewRepository;
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.configRepository = configRepository;
    }

    /**
     * Supprime tout le contenu (avis, menus/carte + sections, plats) mais
     * conserve la configuration du site.
     */
    @Transactional
    public void clearContent() {
        reviewRepository.deleteAll();
        // Les sections (et les liens section_dish) sont supprimees en cascade
        // via la suppression des menus, avant de supprimer les plats.
        menuRepository.deleteAll();
        dishRepository.deleteAll();
    }

    /**
     * Reinitialisation totale : contenu + configuration. Le site repassera
     * par la page de configuration initiale.
     */
    @Transactional
    public void resetAll() {
        clearContent();
        configRepository.deleteAll();
    }
}
