package com.myrestaurant.service;

import com.myrestaurant.domain.Review;
import com.myrestaurant.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * Gestion des avis clients.
 */
@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Review add(Review review) {
        if (review.getDate() == null) {
            review.setDate(LocalDate.now());
        }
        return repository.save(review);
    }

    @Transactional(readOnly = true)
    public List<Review> latest(int count) {
        return repository.findByOrderByDateDescIdDesc(PageRequest.of(0, count));
    }

    @Transactional(readOnly = true)
    public List<Review> all() {
        return repository.findAllByOrderByDateDescIdDesc();
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
