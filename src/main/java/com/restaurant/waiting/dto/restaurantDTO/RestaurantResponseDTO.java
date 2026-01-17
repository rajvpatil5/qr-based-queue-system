package com.restaurant.waiting.dto.restaurantDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDTO {
    private Long id;
    private String restaurantCode;
    private String name;
    private String city;
    private String address;
    private String phone;
    private boolean active;
}
