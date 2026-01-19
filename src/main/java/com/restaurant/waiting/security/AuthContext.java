package com.restaurant.waiting.security;

import com.restaurant.waiting.model.restaurant.Restaurant;
import com.restaurant.waiting.model.restaurant.RestaurantUser;
import com.restaurant.waiting.repository.RestaurantRepository;
import com.restaurant.waiting.repository.RestaurantUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthContext {

    private final RestaurantUserRepository restaurantUserRepository;

    private final RestaurantRepository restaurantRepository;

    public String getKeycloakUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return jwt.getSubject();
    }

    public RestaurantUser requireRestaurantUser() {
        return restaurantUserRepository
                .findByKeycloakUserId(getKeycloakUserId())
                .orElseThrow(() ->
                        new AccessDeniedException("Restaurant onboarding required")
                );
    }

    /**
     * CRITICAL: Ensures the URL restaurantCode belongs to the logged-in user
     */
    public Restaurant requireRestaurant(String restaurantCode) {

        RestaurantUser user = requireRestaurantUser();
        Restaurant restaurant = user.getRestaurant();

        if (!restaurant.getRestaurantCode().equals(restaurantCode)) {
            throw new AccessDeniedException("Restaurant access denied");
        }

        return restaurant;
    }
}

