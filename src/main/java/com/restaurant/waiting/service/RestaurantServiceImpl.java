package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.restaurantDTO.RestaurantCreateRequestDTO;
import com.restaurant.waiting.dto.restaurantDTO.RestaurantResponseDTO;
import com.restaurant.waiting.mapper.RestaurantMapper;
import com.restaurant.waiting.model.Restaurant;
import com.restaurant.waiting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    public RestaurantRepository restaurantRepository;
    public RestaurantMapper mapper;

    // Create restaurant
    public RestaurantResponseDTO create(RestaurantCreateRequestDTO restaurantCreateRequestDTO) {
        Restaurant restaurantEntity = mapper.toRestaurantEntity(restaurantCreateRequestDTO);
        Restaurant dbRestaurant = restaurantRepository.save(restaurantEntity);
        log.info("RestaurantServiceImpl::create -> dbRestaurant = {}", dbRestaurant);
        return mapper.toRestaurantResponse(dbRestaurant);
    }

    // Activate account
    public RestaurantResponseDTO activate(String restaurantCode) {
        Restaurant r = getOrThrow(restaurantCode);
        r.setActive(true);
        return mapper.toRestaurantResponse(restaurantRepository.save(r));
    }


    public Restaurant getOrThrow(String restaurantCode) {
        return restaurantRepository.findByRestaurantCode(restaurantCode).orElseThrow(() -> new RuntimeException("Restaurant Not Found."));
    }

    // Get single restaurant
    public RestaurantResponseDTO get(String restaurantCode) {
        return mapper.toRestaurantResponse(getOrThrow(restaurantCode));
    }

    // Get restaurant list
    public List<RestaurantResponseDTO> getRestaurantList() {
        List<Restaurant> allRestaurant = restaurantRepository.findAll();
        return allRestaurant.stream().map(mapper::toRestaurantResponse).toList();
    }

    // deactivate restaurant
    public RestaurantResponseDTO deactivate(String restaurantCode) {
        Restaurant restaurant = getOrThrow(restaurantCode);
        restaurant.setActive(false);
        Restaurant dbRestaurant = restaurantRepository.save(restaurant);
        return mapper.toRestaurantResponse(dbRestaurant);
    }

    public RestaurantResponseDTO updateRestaurant(String restaurantCode, RestaurantCreateRequestDTO restaurantCreateRequestDTO) {
        Restaurant restaurant = getOrThrow(restaurantCode);
        mapper.updateRestaurantEntityFromRequest(restaurantCreateRequestDTO, restaurant);
        return mapper.toRestaurantResponse(restaurant);
    }
}
