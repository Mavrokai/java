package com.myrestaurant.web.admin;

import com.myrestaurant.service.DishService;
import com.myrestaurant.service.MenuService;
import com.myrestaurant.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MenuService menuService;
    private final DishService dishService;
    private final ReviewService reviewService;

    public AdminController(MenuService menuService, DishService dishService, ReviewService reviewService) {
        this.menuService = menuService;
        this.dishService = dishService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("dishCount", dishService.listDishes().size());
        model.addAttribute("menuCount", menuService.listMenus().size());
        model.addAttribute("reviewCount", reviewService.all().size());
        model.addAttribute("carte", menuService.getOrCreateCarte());
        return "admin/dashboard";
    }
}
