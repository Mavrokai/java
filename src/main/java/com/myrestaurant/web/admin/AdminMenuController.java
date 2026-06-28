package com.myrestaurant.web.admin;

import com.myrestaurant.domain.Menu;
import com.myrestaurant.domain.Section;
import com.myrestaurant.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Gestion CRUD de la carte, des menus et de leurs sections.
 */
@Controller
@RequestMapping("/admin")
public class AdminMenuController {

    private final MenuService menuService;

    public AdminMenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    // ----- La carte -----

    @GetMapping("/carte")
    public String carte(Model model) {
        Menu carte = menuService.getOrCreateCarte();
        model.addAttribute("menu", carte);
        model.addAttribute("isCarte", true);
        return "admin/menu-edit";
    }

    // ----- Les menus -----

    @GetMapping("/menus")
    public String menus(Model model) {
        model.addAttribute("menus", menuService.listMenus());
        return "admin/menus";
    }

    @PostMapping("/menus")
    public String createMenu(@RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Double price,
                             @RequestParam(defaultValue = "0") int position,
                             RedirectAttributes ra) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDescription(description);
        menu.setPrice(price);
        menu.setPosition(position);
        menu.setCarte(false);
        Menu saved = menuService.saveMenu(menu);
        ra.addFlashAttribute("flash", "Menu cree.");
        return "redirect:/admin/menus/" + saved.getId();
    }

    @GetMapping("/menus/{id}")
    public String editMenu(@PathVariable Long id, Model model) {
        Menu menu = menuService.getMenu(id);
        model.addAttribute("menu", menu);
        model.addAttribute("isCarte", menu.isCarte());
        return "admin/menu-edit";
    }

    @PostMapping("/menus/{id}")
    public String updateMenu(@PathVariable Long id,
                             @RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) Double price,
                             @RequestParam(defaultValue = "0") int position,
                             RedirectAttributes ra) {
        Menu menu = menuService.getMenu(id);
        menu.setName(name);
        menu.setDescription(description);
        menu.setPrice(price);
        menu.setPosition(position);
        menuService.saveMenu(menu);
        ra.addFlashAttribute("flash", "Menu mis a jour.");
        return "redirect:/admin/menus/" + id;
    }

    @PostMapping("/menus/{id}/delete")
    public String deleteMenu(@PathVariable Long id, RedirectAttributes ra) {
        menuService.deleteMenu(id);
        ra.addFlashAttribute("flash", "Menu supprime.");
        return "redirect:/admin/menus";
    }

    // ----- Les sections -----

    @PostMapping("/menus/{menuId}/sections")
    public String addSection(@PathVariable Long menuId,
                             @RequestParam String name,
                             @RequestParam(defaultValue = "0") int position,
                             RedirectAttributes ra) {
        menuService.addSection(menuId, name, position);
        ra.addFlashAttribute("flash", "Section ajoutee.");
        return redirectToOwner(menuId);
    }

    @PostMapping("/sections/{id}")
    public String updateSection(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam(defaultValue = "0") int position,
                                RedirectAttributes ra) {
        Section section = menuService.getSection(id);
        section.setName(name);
        section.setPosition(position);
        menuService.saveSection(section);
        ra.addFlashAttribute("flash", "Section mise a jour.");
        return redirectToOwner(section.getMenu().getId());
    }

    @PostMapping("/sections/{id}/delete")
    public String deleteSection(@PathVariable Long id, RedirectAttributes ra) {
        Section section = menuService.getSection(id);
        Long menuId = section.getMenu().getId();
        menuService.deleteSection(id);
        ra.addFlashAttribute("flash", "Section supprimee.");
        return redirectToOwner(menuId);
    }

    private String redirectToOwner(Long menuId) {
        Menu menu = menuService.getMenu(menuId);
        return menu.isCarte() ? "redirect:/admin/carte" : "redirect:/admin/menus/" + menuId;
    }
}
