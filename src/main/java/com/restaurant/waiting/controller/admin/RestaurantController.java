package com.restaurant.waiting.controller.admin;

import com.restaurant.waiting.dto.restaurantDTO.RestaurantCreateRequestDTO;
import com.restaurant.waiting.dto.restaurantDTO.RestaurantResponseDTO;
import com.restaurant.waiting.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants")
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // register and create restaurant
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> create(
            @Valid @RequestBody RestaurantCreateRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.create(req));
    }

    @PostMapping("/{restaurantCode}/activate")
    public ResponseEntity<RestaurantResponseDTO> activate(@PathVariable String restaurantCode) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.activate(restaurantCode));
    }

//    @GetMapping
//    public ResponseEntity<List<RestaurantResponseDTO>> list() {
//        return ResponseEntity.ok().body(restaurantService.getRestaurantList());
//    }

    @GetMapping("/{restaurantCode}")
    public ResponseEntity<RestaurantResponseDTO> get(@PathVariable String restaurantCode) {
        return ResponseEntity.ok(restaurantService.get(restaurantCode));
    }

    @PutMapping("/{restaurantCode}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(@PathVariable String restaurantCode,
                                                                  @Valid @RequestBody RestaurantCreateRequestDTO restaurantCreateRequestDTO) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurantCode, restaurantCreateRequestDTO));
    }

    @DeleteMapping("/{restaurantCode}")
    public ResponseEntity<Void> deactivate(@PathVariable String restaurantCode) {
        restaurantService.deactivate(restaurantCode);
        return ResponseEntity.noContent().build();
    }
}
