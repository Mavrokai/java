package com.myrestaurant.web.admin;

import com.myrestaurant.service.DataResetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Actions de maintenance des donnees (zone de danger du tableau de bord).
 */
@Controller
@RequestMapping("/admin/data")
public class AdminDataController {

    private final DataResetService dataResetService;

    public AdminDataController(DataResetService dataResetService) {
        this.dataResetService = dataResetService;
    }

    @PostMapping("/clear-content")
    public String clearContent(RedirectAttributes ra) {
        dataResetService.clearContent();
        ra.addFlashAttribute("flash", "Tout le contenu a ete supprime (menus, plats, avis).");
        return "redirect:/admin";
    }

    @PostMapping("/reset-all")
    public String resetAll() {
        dataResetService.resetAll();
        // Le site n'est plus configure : redirection vers la page de configuration.
        return "redirect:/setup";
    }
}
