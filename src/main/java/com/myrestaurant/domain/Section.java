package com.myrestaurant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Section d'une carte ou d'un menu (ex: "Entrees", "Sushis", "Desserts").
 * Contient un ensemble de plats (relation many-to-many : un plat peut etre
 * present dans plusieurs sections de la carte et/ou de plusieurs menus).
 */
@Entity
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int position = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "section_dish",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    @OrderBy("name ASC")
    private Set<Dish> dishes = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }
}
