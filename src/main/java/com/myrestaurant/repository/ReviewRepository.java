package com.myrestaurant.repository;

import com.myrestaurant.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByDateDescIdDesc();

    List<Review> findByOrderByDateDescIdDesc(Pageable pageable);
}
