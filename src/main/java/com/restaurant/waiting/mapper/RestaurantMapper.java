package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.restaurantDTO.RestaurantCreateRequestDTO;
import com.restaurant.waiting.dto.restaurantDTO.RestaurantResponseDTO;
import com.restaurant.waiting.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    /* ENTITY → RESPONSE */
    RestaurantResponseDTO toRestaurantResponse(Restaurant restaurant);

    /* CREATE (only map user-editable fields) */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantCode", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toRestaurantEntity(RestaurantCreateRequestDTO restaurantCreateRequestDTO);

    /* UPDATE (only update fields allowed to change) */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurantCode", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateRestaurantEntityFromRequest(RestaurantCreateRequestDTO request, @MappingTarget Restaurant restaurant);
}
