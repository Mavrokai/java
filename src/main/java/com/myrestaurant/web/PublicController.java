package com.myrestaurant.web;

import com.myrestaurant.domain.Review;
import com.myrestaurant.service.MenuService;
import com.myrestaurant.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Pages publiques destinees aux clients du restaurant.
 */
@Controller
public class PublicController {

    private final MenuService menuService;
    private final ReviewService reviewService;

    public PublicController(MenuService menuService, ReviewService reviewService) {
        this.menuService = menuService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("reviews", reviewService.latest(4));
        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new Review());
        }
        return "public/home";
    }

    @GetMapping("/carte")
    public String carte(Model model) {
        model.addAttribute("carte", menuService.getOrCreateCarte());
        return "public/carte";
    }

    @GetMapping("/menus")
    public String menus(Model model) {
        model.addAttribute("menus", menuService.listMenus());
        return "public/menus";
    }

    @PostMapping("/reviews")
    public String addReview(@Valid @ModelAttribute("review") Review review,
                            BindingResult binding,
                            RedirectAttributes ra) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            binding.rejectValue("rating", "invalid", "Merci de donner une note entre 1 et 5 etoiles");
        }
        if (review.getAuthorName() == null || review.getAuthorName().isBlank()) {
            binding.rejectValue("authorName", "required", "Le nom est obligatoire");
        }
        if (binding.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.review", binding);
            ra.addFlashAttribute("review", review);
            ra.addFlashAttribute("reviewError", true);
            return "redirect:/#avis";
        }
        reviewService.add(review);
        ra.addFlashAttribute("reviewSuccess", true);
        return "redirect:/#avis";
    }
}
