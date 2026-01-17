package com.restaurant.waiting.repository;

import com.restaurant.waiting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByRestaurantCode(String restaurantCode);

    boolean existsByRestaurantCode(String restaurantCode);
}
