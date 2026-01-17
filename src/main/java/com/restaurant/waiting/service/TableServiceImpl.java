package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.tableDTO.TableAllocationResponse;
import com.restaurant.waiting.dto.tableDTO.TableBulkCreateRequest;
import com.restaurant.waiting.dto.tableDTO.TableResponse;
import com.restaurant.waiting.mapper.TableMapper;
import com.restaurant.waiting.model.*;
import com.restaurant.waiting.repository.RestaurantRepository;
import com.restaurant.waiting.repository.TableRepository;
import com.restaurant.waiting.repository.WaitEntryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;
    private final WaitEntryRepository waitEntryRepository;
    private final TableMapper mapper;

    @Override
    public void seatCustomer(String restaurantCode, Long tableId) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
        Table table = tableRepository.findByIdAndRestaurant(tableId, restaurant).orElseThrow(() -> new IllegalArgumentException("Table not found"));
        if (table.getStatus() != TableStatus.RESERVED) {
            throw new IllegalStateException("Table not reserved");
        }
        WaitEntry waitEntry = waitEntryRepository.findNotifiedForTable(restaurant.getId(), tableId).orElseThrow(() ->
                new IllegalStateException("No notified customer for this table"));
        waitEntry.markSeated();
        table.markOccupied();
    }

    @Override
    public List<TableResponse> bulkCreate(TableBulkCreateRequest req, String restaurantCode) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
        if (req.getStartNumber() > req.getEndNumber()) {
            throw new RuntimeException("Invalid Range");
        }
        List<Table> tables = new ArrayList<>();
        for (int i = req.getStartNumber(); i <= req.getEndNumber(); i++) {
            if (tableRepository.existsByRestaurantAndTableNumber(restaurant, i)) {
                throw new IllegalStateException("Table number already exists: " + i);
            }
            Table table = new Table();
            table.setTableNumber(i);
            table.setCapacity(req.getCapacity());
            table.setFamilyOnly(req.isLadiesOnly());
            table.setRestaurant(restaurant);

            tables.add(table);
        }
        return tableRepository.saveAll(tables).stream().map(mapper::toTableResponse).toList();
    }

    @Override
    public List<TableResponse> findAll(String restaurantCode) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
        List<Table> allTables = tableRepository.findAll();
        return mapper.toTableResponseList(allTables);
    }

    /**
     * AVAILABLE -> RESERVED
     */
    @Override
    @Transactional
    public Optional<TableAllocationResponse> allocate(
            String restaurantCode,
            Long tableId
    ) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
        log.info("TableServiceImpl::allocate -> restaurant = {}", restaurant);

        Table table = tableRepository
                .findByIdAndRestaurant(tableId, restaurant)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table must be AVAILABLE to allocate");
        }

        log.info("TableServiceImpl::allocate -> table.getCapacity() = {}", table.getCapacity());
        log.info("TableServiceImpl::allocate -> table.isFamilyOnly() = {}", table.isFamilyOnly());
        Optional<WaitEntry> next =
                waitEntryRepository.findNextEligibleForTable(
                                restaurant,
                                table.getCapacity(),
                                table.isFamilyOnly(), PageRequest.of(0, 1)
                        ).stream()
                        .findFirst();

        if (next.isEmpty()) {
            log.info("TableServiceImpl::allocate -> next.isEmpty() = {}", next.isEmpty());
            return Optional.empty();
        }

        WaitEntry entry = next.get();

        // atomic transition
        entry.markNotified(table);
        table.markReserved();

        return Optional.of(
                mapper.toTableAllocationResponse(entry, table)
        );
    }


    /**
     * RESERVED -> OCCUPIED
     */
//    @Override
//    @Transactional
//    public void markOccupied(
//            String restaurantCode,
//            Long tableId
//    ) {
//        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
//
//        Table table = tableRepository
//                .findByIdAndRestaurant(tableId, restaurant)
//                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
//
//        if (table.getStatus() != TableStatus.RESERVED) {
//            throw new IllegalStateException(
//                    "Table must be RESERVED to mark OCCUPIED"
//            );
//        }
//
//        table.markOccupied();
//    }

    /**
     * OCCUPIED -> AVAILABLE -> (optional) RESERVED
     */
    @Override
    @Transactional
    public Optional<TableAllocationResponse> releaseAndAllocate(
            String restaurantCode,
            Long tableId
    ) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);

        Table table = tableRepository
                .findByIdAndRestaurant(tableId, restaurant)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        if (table.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalStateException(
                    "Table must be OCCUPIED to release"
            );
        }
        // 1️⃣ Find seated customer
        WaitEntry seated = waitEntryRepository
                .findSeatedByTable(table)
                .orElseThrow(() -> new IllegalStateException("No seated customer"));

        // 2️⃣ Complete customer lifecycle
        seated.markCompleted();

        // 3️⃣ Free table
        table.markAvailable();

        List<WaitEntry> waitingCustomers = waitEntryRepository.findNextEligibleForTable(
                restaurant,
                table.getCapacity(),
                table.isFamilyOnly(), PageRequest.of(0, 1)
        );

        if (waitingCustomers.isEmpty()) {
            return Optional.empty();
        }

        WaitEntry entry = waitingCustomers.getFirst();
        entry.markNotified(table);
        table.markReserved();

        return Optional.of(
                mapper.toTableAllocationResponse(entry, table)
        );
    }

 
}
