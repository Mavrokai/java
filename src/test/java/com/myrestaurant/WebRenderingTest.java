package com.myrestaurant;

import com.myrestaurant.service.ConfigService;
import com.myrestaurant.web.form.SetupForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

/**
 * Verifie que toutes les pages (publiques et admin) se rendent sans erreur
 * de template Thymeleaf.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebRenderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfigService configService;

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
            form.setPracticalInfo("Mardi-Dimanche 12h-14h30 / 19h-22h30");
            configService.saveSetup(form, null);
        }
    }

    @Test
    void publicPagesRender() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/carte")).andExpect(status().isOk());
        mockMvc.perform(get("/menus")).andExpect(status().isOk());
    }

    @Test
    void placeholderServesSvg() throws Exception {
        mockMvc.perform(get("/placeholder").param("label", "Ramen"))
                .andExpect(status().isOk());
    }

    @Test
    void adminRequiresAuth() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void anonymousHeaderShowsLoginNotAdmin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Connexion")))
                .andExpect(content().string(not(containsString("Déconnexion"))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void authenticatedHeaderShowsAdminAndLogout() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(">Admin</a>")))
                .andExpect(content().string(containsString("Déconnexion")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminPagesRender() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/carte")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/menus")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/dishes")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/dishes/new")).andExpect(status().isOk());
        mockMvc.perform(get("/admin/reviews")).andExpect(status().isOk());
    }
}
