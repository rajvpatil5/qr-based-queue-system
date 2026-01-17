package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.tableDTO.TableAllocationResponse;
import com.restaurant.waiting.dto.tableDTO.TableBulkCreateRequest;
import com.restaurant.waiting.dto.tableDTO.TableResponse;

import java.util.List;
import java.util.Optional;

public interface TableService {
//    Optional<TableAllocationResponse> handleTableAvailable(Long tableId);
//
//    void handleTableOccupied(Long tableId);
//    void markOccupied(String restaurantCode, Long tableId);

    List<TableResponse> bulkCreate(TableBulkCreateRequest tableBulkCreateRequest, String restaurantCode);

    List<TableResponse> findAll(String restaurantCode);

    void seatCustomer(String restaurantCode, Long tableId);

    Optional<TableAllocationResponse> allocate(String restaurantCode, Long tableId);

    Optional<TableAllocationResponse> releaseAndAllocate(String restaurantCode, Long tableId);

    void forceNotify(String restaurantCode, Long tableId, String trackingCode);

}
