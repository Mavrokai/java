package com.myrestaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * Avis laisse par un client sur la page d'accueil.
 */
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String authorName;

    /** Email optionnel. */
    @Email(message = "Email invalide")
    private String email;

    /** Date prereplie a aujourd'hui. */
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date = LocalDate.now();

    /** Note en etoiles (1 a 5). */
    @Column(nullable = false)
    private int rating;

    /** Commentaire optionnel. */
    @Column(length = 2000)
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
