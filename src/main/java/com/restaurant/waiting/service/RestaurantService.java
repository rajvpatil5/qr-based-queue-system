package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.restaurantDTO.RestaurantCreateRequestDTO;
import com.restaurant.waiting.dto.restaurantDTO.RestaurantResponseDTO;
import com.restaurant.waiting.model.restaurant.Restaurant;

import java.util.List;

public interface RestaurantService {
    RestaurantResponseDTO create(RestaurantCreateRequestDTO restaurantCreateRequestDTO);

    RestaurantResponseDTO get(String restaurantCode);

    List<RestaurantResponseDTO> getRestaurantList();

    RestaurantResponseDTO deactivate(String restaurantCode);

    RestaurantResponseDTO updateRestaurant(String restaurantCode,
                                           RestaurantCreateRequestDTO restaurantCreateRequestDTO);

    RestaurantResponseDTO activate(String restaurantCode);

    Restaurant getOrThrow(String restaurantCode);

    Restaurant getOrThrowPublic(String restaurantCode);
}
