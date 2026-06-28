package com.myrestaurant.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

/**
 * Genere des images placeholder SVG (hors-ligne) en attendant les vraies photos.
 * La couleur de fond est derivee du nom pour un rendu varie mais stable.
 */
@Service
public class PlaceholderService {

    /** Palette douce inspiree d'une ambiance japonaise epuree. */
    private static final String[] PALETTE = {
            "#d9c9b6", "#c2b8a3", "#b3a394", "#a8b5a2",
            "#c9b1a0", "#9fb3c8", "#c8a9a3", "#b6b09c"
    };

    public byte[] svg(String label, int width, int height) {
        String text = label == null || label.isBlank() ? "My Restaurant" : label.trim();
        String bg = PALETTE[Math.floorMod(text.hashCode(), PALETTE.length)];
        String initials = initials(text);
        String svg = """
                <svg xmlns="http://www.w3.org/2000/svg" width="%d" height="%d" viewBox="0 0 %d %d">
                  <rect width="100%%" height="100%%" fill="%s"/>
                  <circle cx="%d" cy="%d" r="%d" fill="rgba(255,255,255,0.18)"/>
                  <text x="50%%" y="50%%" dominant-baseline="middle" text-anchor="middle"
                        font-family="Georgia, serif" font-size="%d" fill="rgba(40,30,25,0.65)">%s</text>
                </svg>
                """.formatted(
                width, height, width, height,
                bg,
                width / 2, height / 2, Math.min(width, height) / 3,
                Math.min(width, height) / 4, escape(initials));
        return svg.getBytes(StandardCharsets.UTF_8);
    }

    private String initials(String text) {
        String[] parts = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (!p.isBlank() && sb.length() < 2) {
                sb.append(Character.toUpperCase(p.charAt(0)));
            }
        }
        return sb.length() == 0 ? "?" : sb.toString();
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
