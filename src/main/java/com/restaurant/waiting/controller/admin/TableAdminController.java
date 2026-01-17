package com.restaurant.waiting.controller.admin;

import com.restaurant.waiting.dto.tableDTO.TableAllocationResponse;
import com.restaurant.waiting.dto.tableDTO.TableBulkCreateRequest;
import com.restaurant.waiting.dto.tableDTO.TableResponse;
import com.restaurant.waiting.service.TableService;
import com.restaurant.waiting.service.WaitEntryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin controller for table-related actions (hotel staff on each floor).
 */
@RestController
@RequestMapping("/api/admin/restaurants/{restaurantCode}/tables")
@Slf4j
public class TableAdminController {

    private final TableService tableService;
    private final WaitEntryService waitEntryService;

    public TableAdminController(TableService tableService, WaitEntryService waitEntryService) {
        this.tableService = tableService;
        this.waitEntryService = waitEntryService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<TableResponse>> bulkCreate(@PathVariable String restaurantCode,
                                                          @Valid @RequestBody TableBulkCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tableService.bulkCreate(req, restaurantCode));
    }

    @PostMapping("/{tableId}/force-notify/{trackingCode}")
    public ResponseEntity<TableAllocationResponse> forceNotify(@PathVariable String restaurantCode,
                                                               @PathVariable Long tableId,
                                                               @PathVariable String trackingCode) {
        tableService.forceNotify(restaurantCode, tableId, trackingCode);
        return ResponseEntity.ok().build();
    }

    /**
     * Allocate an AVAILABLE table to next eligible waiting customer AVAILABLE -> RESERVED
     */
    @PostMapping("/{tableId}/allocate")
    public ResponseEntity<TableAllocationResponse> allocate(@PathVariable String restaurantCode,
                                                            @PathVariable Long tableId) {
        return tableService.allocate(restaurantCode, tableId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    //    RESERVED -> OCCUPIED
    @PostMapping("/{tableId}/seat")
    public ResponseEntity<Void> seatCustomer(@PathVariable String restaurantCode, @PathVariable Long tableId) {
        tableService.seatCustomer(restaurantCode, tableId);
        return ResponseEntity.ok().build();
    }

    /**
     * Customer leaves table OCCUPIED -> AVAILABLE (+ auto allocate)
     */
    @PostMapping("/{tableId}/release")
    public ResponseEntity<TableAllocationResponse> release(@PathVariable String restaurantCode,
                                                           @PathVariable Long tableId) {
        return tableService.releaseAndAllocate(restaurantCode, tableId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> list(@PathVariable String restaurantCode) {
        return ResponseEntity.ok(tableService.findAll(restaurantCode));
    }

}