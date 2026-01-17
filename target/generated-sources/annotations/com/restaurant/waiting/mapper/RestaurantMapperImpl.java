package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.restaurantDTO.RestaurantCreateRequestDTO;
import com.restaurant.waiting.dto.restaurantDTO.RestaurantResponseDTO;
import com.restaurant.waiting.model.Restaurant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-17T15:02:41+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public RestaurantResponseDTO toRestaurantResponse(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();

        restaurantResponseDTO.setId( restaurant.getId() );
        restaurantResponseDTO.setRestaurantCode( restaurant.getRestaurantCode() );
        restaurantResponseDTO.setName( restaurant.getName() );
        restaurantResponseDTO.setCity( restaurant.getCity() );
        restaurantResponseDTO.setAddress( restaurant.getAddress() );
        restaurantResponseDTO.setPhone( restaurant.getPhone() );
        restaurantResponseDTO.setActive( restaurant.isActive() );

        return restaurantResponseDTO;
    }

    @Override
    public Restaurant toRestaurantEntity(RestaurantCreateRequestDTO restaurantCreateRequestDTO) {
        if ( restaurantCreateRequestDTO == null ) {
            return null;
        }

        Restaurant restaurant = new Restaurant();

        restaurant.setName( restaurantCreateRequestDTO.getName() );
        restaurant.setAddress( restaurantCreateRequestDTO.getAddress() );
        restaurant.setPhone( restaurantCreateRequestDTO.getPhone() );
        restaurant.setCity( restaurantCreateRequestDTO.getCity() );

        return restaurant;
    }

    @Override
    public void updateRestaurantEntityFromRequest(RestaurantCreateRequestDTO request, Restaurant restaurant) {
        if ( request == null ) {
            return;
        }

        restaurant.setName( request.getName() );
        restaurant.setAddress( request.getAddress() );
        restaurant.setPhone( request.getPhone() );
        restaurant.setCity( request.getCity() );
    }
}
