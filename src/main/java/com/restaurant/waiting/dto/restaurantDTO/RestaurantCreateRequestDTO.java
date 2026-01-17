package com.restaurant.waiting.dto.restaurantDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantCreateRequestDTO {
    private String name;
    private String address;
    private String phone;
    private String city;
}
