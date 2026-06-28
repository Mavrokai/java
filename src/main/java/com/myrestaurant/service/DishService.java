package com.myrestaurant.service;

import com.myrestaurant.domain.Allergen;
import com.myrestaurant.domain.Dish;
import com.myrestaurant.domain.Section;
import com.myrestaurant.repository.DishRepository;
import com.myrestaurant.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Gestion des plats et de leur assignation aux sections.
 */
@Service
public class DishService {

    private final DishRepository dishRepository;
    private final SectionRepository sectionRepository;
    private final StorageService storageService;

    public DishService(DishRepository dishRepository,
                       SectionRepository sectionRepository,
                       StorageService storageService) {
        this.dishRepository = dishRepository;
        this.sectionRepository = sectionRepository;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public List<Dish> listDishes() {
        return dishRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Dish getDish(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plat introuvable: " + id));
    }

    @Transactional
    public Dish save(Dish dish, Set<Allergen> allergens, Set<Long> sectionIds, MultipartFile photo) {
        dish.setAllergens(allergens == null ? EnumSet.noneOf(Allergen.class) : EnumSet.copyOf(
                allergens.isEmpty() ? EnumSet.noneOf(Allergen.class) : allergens));

        String stored = storageService.store(photo);
        if (stored != null) {
            dish.setPhoto(stored);
        }

        Dish saved = dishRepository.save(dish);
        syncSections(saved, sectionIds);
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Dish dish = getDish(id);
        // Detacher le plat de toutes les sections avant suppression.
        for (Section section : sectionRepository.findAll()) {
            if (section.getDishes().remove(dish)) {
                sectionRepository.save(section);
            }
        }
        dishRepository.delete(dish);
    }

    @Transactional(readOnly = true)
    public Set<Long> sectionIdsOf(Long dishId) {
        Dish dish = getDish(dishId);
        java.util.Set<Long> ids = new java.util.HashSet<>();
        for (Section section : sectionRepository.findAll()) {
            if (section.getDishes().stream().anyMatch(d -> d.getId().equals(dish.getId()))) {
                ids.add(section.getId());
            }
        }
        return ids;
    }

    private void syncSections(Dish dish, Set<Long> sectionIds) {
        Set<Long> target = sectionIds == null ? Set.of() : sectionIds;
        for (Section section : sectionRepository.findAll()) {
            boolean shouldContain = target.contains(section.getId());
            boolean contains = section.getDishes().stream().anyMatch(d -> d.getId().equals(dish.getId()));
            if (shouldContain && !contains) {
                section.getDishes().add(dish);
                sectionRepository.save(section);
            } else if (!shouldContain && contains) {
                section.getDishes().removeIf(d -> d.getId().equals(dish.getId()));
                sectionRepository.save(section);
            }
        }
    }
}
