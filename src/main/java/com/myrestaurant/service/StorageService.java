package com.myrestaurant.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Stockage des fichiers uploades (bannieres, photos de plats) sur le disque.
 */
@Service
public class StorageService {

    private final Path root;

    public StorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible de creer le dossier d'upload", e);
        }
    }

    /**
     * Sauvegarde un fichier et retourne son nom genere, ou null si le fichier est vide.
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String original = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) {
            ext = original.substring(dot).toLowerCase();
        }
        String filename = UUID.randomUUID() + ext;
        try {
            Path target = root.resolve(filename).normalize();
            if (!target.startsWith(root)) {
                throw new IllegalArgumentException("Chemin de fichier invalide");
            }
            Files.copy(file.getInputStream(), target);
            return filename;
        } catch (IOException e) {
            throw new UncheckedIOException("Echec de l'enregistrement du fichier", e);
        }
    }

    public Path getRoot() {
        return root;
    }
}
