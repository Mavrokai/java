package com.myrestaurant.web;

import com.myrestaurant.domain.RestaurantConfig;
import com.myrestaurant.service.ConfigService;
import com.myrestaurant.web.form.SetupForm;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Page de configuration initiale. Accessible librement tant que le site n'est
 * pas configure ; ensuite reservee a l'administrateur (re-configuration).
 */
@Controller
public class SetupController {

    private final ConfigService configService;

    public SetupController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/setup")
    public String setupForm(Model model, Authentication auth) {
        boolean configured = configService.isConfigured();
        boolean isAdmin = auth != null && auth.isAuthenticated()
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (configured && !isAdmin) {
            return "redirect:/";
        }

        if (!model.containsAttribute("setupForm")) {
            SetupForm form = new SetupForm();
            configService.getConfig().ifPresent(c -> {
                form.setName(c.getName());
                form.setUrl(c.getUrl());
                form.setThemeColor(c.getThemeColor());
                form.setAddress(c.getAddress());
                form.setPhone(c.getPhone());
                form.setEmail(c.getEmail());
                form.setPracticalInfo(c.getPracticalInfo());
            });
            if (form.getUrl() == null) {
                form.setUrl("http://localhost:8080");
            }
            model.addAttribute("setupForm", form);
        }
        model.addAttribute("alreadyConfigured", configured);
        return "setup";
    }

    @PostMapping("/setup")
    public String submit(@Valid @ModelAttribute("setupForm") SetupForm form,
                         BindingResult binding,
                         @RequestParam(value = "banner", required = false) MultipartFile banner,
                         Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("alreadyConfigured", configService.isConfigured());
            return "setup";
        }
        RestaurantConfig saved = configService.saveSetup(form, banner);
        model.addAttribute("config", saved);
        return "redirect:/";
    }
}
