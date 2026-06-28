package com.myrestaurant.service;

import com.myrestaurant.domain.Menu;
import com.myrestaurant.domain.Section;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Gestion de la carte (singleton), des menus et de leurs sections.
 */
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final SectionRepository sectionRepository;

    public MenuService(MenuRepository menuRepository, SectionRepository sectionRepository) {
        this.menuRepository = menuRepository;
        this.sectionRepository = sectionRepository;
    }

    /** Retourne la carte, en la creant si necessaire. */
    @Transactional
    public Menu getOrCreateCarte() {
        return menuRepository.findFirstByCarteTrue().orElseGet(() -> {
            Menu carte = new Menu();
            carte.setName("La carte");
            carte.setCarte(true);
            return menuRepository.save(carte);
        });
    }

    @Transactional(readOnly = true)
    public List<Menu> listMenus() {
        return menuRepository.findByCarteFalseOrderByPositionAsc();
    }

    @Transactional(readOnly = true)
    public Menu getMenu(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu introuvable: " + id));
    }

    @Transactional
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Transactional
    public void deleteMenu(Long id) {
        Menu menu = getMenu(id);
        if (menu.isCarte()) {
            throw new IllegalArgumentException("La carte ne peut pas etre supprimee");
        }
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public List<Section> sectionsOf(Long menuId) {
        return sectionRepository.findByMenuIdOrderByPositionAsc(menuId);
    }

    @Transactional(readOnly = true)
    public Section getSection(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section introuvable: " + id));
    }

    @Transactional
    public Section addSection(Long menuId, String name, int position) {
        Menu menu = getMenu(menuId);
        Section section = new Section();
        section.setMenu(menu);
        section.setName(name);
        section.setPosition(position);
        return sectionRepository.save(section);
    }

    @Transactional
    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    @Transactional
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }
}
