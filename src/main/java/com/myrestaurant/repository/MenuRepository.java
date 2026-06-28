package com.myrestaurant.repository;

import com.myrestaurant.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findFirstByCarteTrue();

    List<Menu> findByCarteFalseOrderByPositionAsc();
}
