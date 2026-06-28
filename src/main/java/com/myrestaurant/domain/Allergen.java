package com.myrestaurant.domain;

/**
 * Liste reglementaire des 14 allergenes a declaration obligatoire (UE / France).
 * Reference : https://goo.gl/nTA9tn
 */
public enum Allergen {
    GLUTEN("Gluten"),
    CRUSTACES("Crustaces"),
    OEUFS("Oeufs"),
    POISSONS("Poissons"),
    ARACHIDES("Arachides"),
    SOJA("Soja"),
    LAIT("Lait"),
    FRUITS_A_COQUE("Fruits a coque"),
    CELERI("Celeri"),
    MOUTARDE("Moutarde"),
    SESAME("Graines de sesame"),
    SULFITES("Anhydride sulfureux et sulfites"),
    LUPIN("Lupin"),
    MOLLUSQUES("Mollusques");

    private final String label;

    Allergen(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
