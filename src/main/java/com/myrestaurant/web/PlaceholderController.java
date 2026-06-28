package com.myrestaurant.web;

import com.myrestaurant.service.PlaceholderService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.Duration;

/**
 * Sert des images placeholder SVG generees a la volee.
 */
@RestController
public class PlaceholderController {

    private final PlaceholderService placeholderService;

    public PlaceholderController(PlaceholderService placeholderService) {
        this.placeholderService = placeholderService;
    }

    @GetMapping(value = "/placeholder", produces = "image/svg+xml")
    public ResponseEntity<byte[]> placeholder(
            @RequestParam(defaultValue = "My Restaurant") String label,
            @RequestParam(defaultValue = "600") int w,
            @RequestParam(defaultValue = "400") int h) {
        byte[] svg = placeholderService.svg(label, clamp(w), clamp(h));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .cacheControl(CacheControl.maxAge(Duration.ofDays(1)))
                .body(svg);
    }

    private int clamp(int v) {
        return Math.max(40, Math.min(2000, v));
    }
}
