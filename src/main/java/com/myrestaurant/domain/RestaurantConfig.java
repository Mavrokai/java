package com.myrestaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Configuration globale du site (entite singleton, id = 1).
 * Tant qu'aucune ligne marquee "configured" n'existe, le site redirige vers /setup.
 */
@Entity
@Table(name = "restaurant_config")
public class RestaurantConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom affiche dans le header et le titre des pages. */
    @Column(nullable = false)
    private String name;

    /** URL publique du site. */
    @Column(nullable = false)
    private String url;

    /** Couleur d'accent (liens, menus, boutons), au format hexadecimal. */
    @Column(nullable = false, length = 7)
    private String themeColor = "#b3001b";

    /** Chemin relatif de la banniere uploadee. */
    private String bannerImage;

    /** Mot de passe administrateur (hashe BCrypt). */
    @Column(nullable = false)
    private String adminPasswordHash;

    // --- Informations restaurant ---
    @Column(nullable = false)
    private String address;

    private String phone;

    private String email;

    /** Horaires / informations pratiques (texte libre). */
    @Column(length = 2000)
    private String practicalInfo;

    /** Drapeau de configuration effective. */
    @Column(nullable = false)
    private boolean configured = false;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getAdminPasswordHash() {
        return adminPasswordHash;
    }

    public void setAdminPasswordHash(String adminPasswordHash) {
        this.adminPasswordHash = adminPasswordHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPracticalInfo() {
        return practicalInfo;
    }

    public void setPracticalInfo(String practicalInfo) {
        this.practicalInfo = practicalInfo;
    }

    public boolean isConfigured() {
        return configured;
    }

    public void setConfigured(boolean configured) {
        this.configured = configured;
    }
}
