package com.myrestaurant;

import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.ReviewRepository;
import com.myrestaurant.service.ConfigService;
import com.myrestaurant.service.DishService;
import com.myrestaurant.web.form.SetupForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie les operations CRUD cote serveur : creation d'un menu et d'un plat
 * en admin, et depot d'un avis cote public (avec validation).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishService dishService;

    @Autowired
    private ReviewRepository reviewRepository;

    @BeforeEach
    void ensureConfigured() {
        if (!configService.isConfigured()) {
            SetupForm form = new SetupForm();
            form.setName("Sakura Izakaya");
            form.setUrl("http://localhost:8080");
            form.setThemeColor("#b3001b");
            form.setAdminPassword("Password1");
            form.setAddress("12 rue de la Paix, 75002 Paris");
            form.setPhone("01 23 45 67 89");
            form.setEmail("contact@sakura.fr");
            form.setPracticalInfo("Mardi-Dimanche");
            configService.saveSetup(form, null);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCreatesMenu() throws Exception {
        long before = menuRepository.count();
        mockMvc.perform(post("/admin/menus")
                        .param("name", "Menu Test")
                        .param("description", "Un menu de test")
                        .param("price", "19.90")
                        .param("position", "5")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/admin/menus/*"));
        assertThat(menuRepository.count()).isEqualTo(before + 1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCreatesDish() throws Exception {
        int before = dishService.listDishes().size();
        mockMvc.perform(post("/admin/dishes")
                        .param("name", "Tofu grille")
                        .param("description", "Tofu mariné")
                        .param("price", "8.50")
                        .param("allergens", "SOJA", "SESAME")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dishes"));
        assertThat(dishService.listDishes().size()).isEqualTo(before + 1);
        assertThat(dishService.listDishes())
                .anyMatch(d -> d.getName().equals("Tofu grille") && d.getAllergens().size() == 2);
    }

    @Test
    void publicAddsValidReview() throws Exception {
        long before = reviewRepository.count();
        mockMvc.perform(post("/reviews")
                        .param("authorName", "Alice")
                        .param("email", "alice@example.com")
                        .param("rating", "5")
                        .param("comment", "Excellent !")
                        .param("date", "2026-06-28")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertThat(reviewRepository.count()).isEqualTo(before + 1);
    }

    @Test
    void publicRejectsInvalidRating() throws Exception {
        long before = reviewRepository.count();
        mockMvc.perform(post("/reviews")
                        .param("authorName", "Bob")
                        .param("rating", "9")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
        // L'avis avec une note hors bornes ne doit pas etre persiste.
        assertThat(reviewRepository.count()).isEqualTo(before);
    }
}
