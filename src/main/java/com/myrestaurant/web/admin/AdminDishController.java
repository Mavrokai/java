package com.myrestaurant.web.admin;

import com.myrestaurant.domain.Allergen;
import com.myrestaurant.domain.Dish;
import com.myrestaurant.domain.Menu;
import com.myrestaurant.service.DishService;
import com.myrestaurant.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gestion CRUD des plats et de leur assignation aux sections.
 */
@Controller
@RequestMapping("/admin/dishes")
public class AdminDishController {

    private final DishService dishService;
    private final MenuService menuService;

    public AdminDishController(DishService dishService, MenuService menuService) {
        this.dishService = dishService;
        this.menuService = menuService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("dishes", dishService.listDishes());
        return "admin/dishes";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("selectedSectionIds", new HashSet<Long>());
        populateForm(model);
        return "admin/dish-edit";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("dish", dishService.getDish(id));
        model.addAttribute("selectedSectionIds", dishService.sectionIdsOf(id));
        populateForm(model);
        return "admin/dish-edit";
    }

    @PostMapping
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam String name,
                       @RequestParam(required = false) String description,
                       @RequestParam double price,
                       @RequestParam(value = "allergens", required = false) List<Allergen> allergens,
                       @RequestParam(value = "sectionIds", required = false) List<Long> sectionIds,
                       @RequestParam(value = "photo", required = false) MultipartFile photo,
                       RedirectAttributes ra) {
        Dish dish = id != null ? dishService.getDish(id) : new Dish();
        dish.setName(name);
        dish.setDescription(description);
        dish.setPrice(price);

        Set<Allergen> allergenSet = allergens == null || allergens.isEmpty()
                ? EnumSet.noneOf(Allergen.class) : EnumSet.copyOf(allergens);
        Set<Long> sectionIdSet = sectionIds == null ? new HashSet<>() : new HashSet<>(sectionIds);

        dishService.save(dish, allergenSet, sectionIdSet, photo);
        ra.addFlashAttribute("flash", "Plat enregistre.");
        return "redirect:/admin/dishes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        dishService.delete(id);
        ra.addFlashAttribute("flash", "Plat supprime.");
        return "redirect:/admin/dishes";
    }

    private void populateForm(Model model) {
        model.addAttribute("allergens", Allergen.values());
        List<Menu> owners = new ArrayList<>();
        owners.add(menuService.getOrCreateCarte());
        owners.addAll(menuService.listMenus());
        model.addAttribute("owners", owners);
    }
}
