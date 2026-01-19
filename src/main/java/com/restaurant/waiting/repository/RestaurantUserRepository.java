package com.restaurant.waiting.repository;

import com.restaurant.waiting.model.restaurant.RestaurantUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantUserRepository extends JpaRepository<RestaurantUser, Long> {

    Optional<RestaurantUser> findByKeycloakUserId(String keycloakUserId);

    boolean existsByKeycloakUserId(String keycloakUserId);
}

