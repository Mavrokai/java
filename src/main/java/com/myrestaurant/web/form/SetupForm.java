package com.myrestaurant.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Formulaire de configuration initiale du site.
 */
public class SetupForm {

    @NotBlank(message = "Le nom du restaurant est obligatoire")
    private String name;

    @NotBlank(message = "L'URL du site est obligatoire")
    private String url;

    @NotBlank
    @Pattern(regexp = "^#([0-9a-fA-F]{6})$", message = "Couleur hexadecimale invalide (ex: #b3001b)")
    private String themeColor = "#b3001b";

    /**
     * Min 8 caracteres, au moins 1 minuscule, 1 majuscule, 1 chiffre.
     */
    @NotBlank(message = "Le mot de passe administrateur est obligatoire")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caracteres, une minuscule, une majuscule et un chiffre"
    )
    private String adminPassword;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    private String phone;

    @Email(message = "Email invalide")
    private String email;

    private String practicalInfo;

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

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
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
}
