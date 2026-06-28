package com.myrestaurant.config;

import com.myrestaurant.domain.Allergen;
import com.myrestaurant.domain.Dish;
import com.myrestaurant.domain.Menu;
import com.myrestaurant.domain.Section;
import com.myrestaurant.repository.DishRepository;
import com.myrestaurant.repository.MenuRepository;
import com.myrestaurant.repository.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.EnumSet;
import java.util.Set;

/**
 * Seed de contenu de demonstration : carte et menus japonais.
 * Ne s'execute que si aucun plat n'existe encore. Ne touche pas a la
 * configuration du site (qui reste a realiser via /setup).
 */
@Component
@Order(2)
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final MenuRepository menuRepository;
    private final SectionRepository sectionRepository;
    private final DishRepository dishRepository;

    public DataSeeder(MenuRepository menuRepository,
                      SectionRepository sectionRepository,
                      DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.sectionRepository = sectionRepository;
        this.dishRepository = dishRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (dishRepository.count() > 0) {
            return;
        }
        log.info("Seed du contenu japonais de demonstration...");

        // --- La carte ---
        Menu carte = menuRepository.findFirstByCarteTrue().orElseGet(() -> {
            Menu c = new Menu();
            c.setName("La carte");
            c.setCarte(true);
            return menuRepository.save(c);
        });

        Section entrees = section(carte, "Entrees", 0);
        Section sushis = section(carte, "Sushis & Makis", 1);
        Section platsChauds = section(carte, "Plats chauds", 2);
        Section desserts = section(carte, "Desserts", 3);

        // --- Plats ---
        Dish edamame = dish("Edamame", "Feves de soja vapeur, fleur de sel.", 4.50,
                EnumSet.of(Allergen.SOJA));
        Dish gyoza = dish("Gyoza au poulet", "Raviolis grilles, sauce ponzu.", 6.90,
                EnumSet.of(Allergen.GLUTEN, Allergen.SOJA, Allergen.SESAME));
        Dish misoSoup = dish("Soupe miso", "Bouillon dashi, tofu, wakame, ciboule.", 4.00,
                EnumSet.of(Allergen.SOJA, Allergen.POISSONS));
        Dish saladeWakame = dish("Salade wakame", "Algues marinees au sesame.", 5.50,
                EnumSet.of(Allergen.SOJA, Allergen.SESAME));

        Dish sushiSaumon = dish("Sushi saumon", "Riz vinaigre, saumon frais (2 pieces).", 5.80,
                EnumSet.of(Allergen.POISSONS));
        Dish sushiThon = dish("Sushi thon", "Riz vinaigre, thon rouge (2 pieces).", 6.20,
                EnumSet.of(Allergen.POISSONS));
        Dish makiConcombre = dish("Maki concombre", "Concombre, sesame, nori (6 pieces).", 4.50,
                EnumSet.of(Allergen.SESAME));
        Dish california = dish("California roll", "Surimi, avocat, mayonnaise (6 pieces).", 6.50,
                EnumSet.of(Allergen.CRUSTACES, Allergen.OEUFS, Allergen.POISSONS, Allergen.SESAME));
        Dish sashimi = dish("Sashimi mix", "Assortiment de poissons crus du jour.", 12.90,
                EnumSet.of(Allergen.POISSONS));

        Dish ramen = dish("Ramen tonkotsu", "Bouillon de porc cremeux, nouilles, oeuf mollet, chashu.", 14.50,
                EnumSet.of(Allergen.GLUTEN, Allergen.OEUFS, Allergen.SOJA, Allergen.SESAME));
        Dish yakitori = dish("Yakitori poulet", "Brochettes de poulet laquees (3 pieces).", 7.50,
                EnumSet.of(Allergen.SOJA, Allergen.GLUTEN, Allergen.SESAME));
        Dish katsuCurry = dish("Katsu curry", "Porc pane, sauce curry japonais, riz.", 13.90,
                EnumSet.of(Allergen.GLUTEN, Allergen.OEUFS, Allergen.SOJA));
        Dish gyudon = dish("Gyudon", "Bol de boeuf mijote au soja sur riz.", 12.50,
                EnumSet.of(Allergen.SOJA, Allergen.GLUTEN, Allergen.SESAME));
        Dish tempura = dish("Tempura de crevettes", "Crevettes en beignet leger (5 pieces).", 9.90,
                EnumSet.of(Allergen.CRUSTACES, Allergen.GLUTEN, Allergen.OEUFS));

        Dish mochi = dish("Mochi glace", "Pate de riz, glace au the vert (2 pieces).", 5.50,
                EnumSet.of(Allergen.LAIT));
        Dish dorayaki = dish("Dorayaki", "Pancakes fourres a la pate de haricot rouge.", 5.00,
                EnumSet.of(Allergen.GLUTEN, Allergen.OEUFS, Allergen.LAIT));
        Dish matcha = dish("The matcha", "The vert japonais en poudre, fouette.", 3.50,
                EnumSet.noneOf(Allergen.class));

        assign(entrees, edamame, gyoza, misoSoup, saladeWakame);
        assign(sushis, sushiSaumon, sushiThon, makiConcombre, california, sashimi);
        assign(platsChauds, ramen, yakitori, katsuCurry, gyudon, tempura);
        assign(desserts, mochi, dorayaki, matcha);

        // --- Menu Decouverte ---
        Menu decouverte = new Menu();
        decouverte.setName("Menu Decouverte");
        decouverte.setDescription("Un parcours complet pour decouvrir nos classiques.");
        decouverte.setPrice(24.90);
        decouverte.setPosition(0);
        decouverte = menuRepository.save(decouverte);
        Section dEntree = section(decouverte, "Pour commencer", 0);
        Section dPlat = section(decouverte, "Plat", 1);
        Section dDessert = section(decouverte, "Dessert", 2);
        assign(dEntree, gyoza, misoSoup);
        assign(dPlat, ramen, gyudon);
        assign(dDessert, mochi);

        // --- Menu Sakura ---
        Menu sakura = new Menu();
        sakura.setName("Menu Sakura");
        sakura.setDescription("Selection autour des sushis et sashimis.");
        sakura.setPrice(29.90);
        sakura.setPosition(1);
        sakura = menuRepository.save(sakura);
        Section sEntree = section(sakura, "Entree", 0);
        Section sPlat = section(sakura, "Assortiment", 1);
        Section sDessert = section(sakura, "Dessert", 2);
        assign(sEntree, edamame, saladeWakame);
        assign(sPlat, sushiSaumon, sushiThon, california, sashimi);
        assign(sDessert, dorayaki, matcha);

        log.info("Seed termine : {} plats crees.", dishRepository.count());
    }

    private Section section(Menu menu, String name, int position) {
        Section s = new Section();
        s.setMenu(menu);
        s.setName(name);
        s.setPosition(position);
        return sectionRepository.save(s);
    }

    private Dish dish(String name, String description, double price, Set<Allergen> allergens) {
        Dish d = new Dish();
        d.setName(name);
        d.setDescription(description);
        d.setPrice(price);
        d.setAllergens(allergens);
        return dishRepository.save(d);
    }

    private void assign(Section section, Dish... dishes) {
        for (Dish d : dishes) {
            section.getDishes().add(d);
        }
        sectionRepository.save(section);
    }
}
