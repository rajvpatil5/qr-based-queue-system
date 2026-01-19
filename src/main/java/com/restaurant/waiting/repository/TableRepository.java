package com.restaurant.waiting.repository;

import com.restaurant.waiting.model.restaurant.Restaurant;
import com.restaurant.waiting.model.table.Table;
import com.restaurant.waiting.model.table.TableStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<Table, Long> {
    boolean existsByRestaurantAndTableNumber(Restaurant restaurant, Integer tableNumber);

    List<Table> findByRestaurantOrderByTableNumberAsc(Restaurant restaurant);

    List<Table> findByRestaurantAndStatus(Restaurant restaurant, TableStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Table> findByIdAndRestaurant(Long id, Restaurant restaurant);
}

