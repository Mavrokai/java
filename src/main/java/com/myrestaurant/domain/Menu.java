package com.myrestaurant.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Represente une carte ou un menu.
 * La "Carte" du restaurant est l'unique Menu ayant carte = true.
 * Les autres sont des menus optionnels ajoutables a la carte.
 */
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    /** Prix global du menu (optionnel, en euros). */
    private Double price;

    @Column(nullable = false)
    private int position = 0;

    /** true => il s'agit de la carte (singleton), false => menu classique. */
    @Column(nullable = false)
    private boolean carte = false;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Section> sections = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCarte() {
        return carte;
    }

    public void setCarte(boolean carte) {
        this.carte = carte;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
