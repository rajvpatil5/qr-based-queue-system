package com.restaurant.waiting.controller.publicapi;

import com.restaurant.waiting.dto.entryDTO.*;
import com.restaurant.waiting.model.WaitStatus;
import com.restaurant.waiting.service.WaitEntryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public/customer-facing controller for waiting operations (QR users).
 */
@RestController
@RequestMapping("/api/public/restaurants/{restaurantCode}/waiting")
@Slf4j
public class WaitingPublicController {

    private final WaitEntryService waitEntryService;

    public WaitingPublicController(WaitEntryService waitEntryService) {
        this.waitEntryService = waitEntryService;
    }

    @PostMapping("/join")
    public ResponseEntity<WaitEntryResponse> joinQueue(@PathVariable String restaurantCode,
                                                       @Valid @RequestBody WaitEntryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(waitEntryService.joinQueue(request, restaurantCode));
    }


    @GetMapping("/{trackingCode}/status")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable String restaurantCode, @PathVariable String trackingCode) {
        return ResponseEntity.ok(waitEntryService.getStatusByTrackingCode(restaurantCode, trackingCode)
        );
    }

    @GetMapping
    public Page<CustomerResponse> getCustomersByStatus(
            @PathVariable String restaurantCode,
            @RequestParam WaitStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return waitEntryService.getCustomersByStatus(
                restaurantCode,
                status,
                page,
                size
        );
    }

    @PutMapping("/{trackingCode}")
    public ResponseEntity<WaitEntryResponse> updateInfo(@Valid @RequestBody WaitEntryUpdateRequest waitEntryUpdateRequest, @PathVariable String restaurantCode, @PathVariable String trackingCode) {
        log.info("WaitingPublicController::updateInfo -> waitEntryUpdateRequest = {}", waitEntryUpdateRequest);

        WaitEntryResponse waitEntryResponseDTO = waitEntryService.updateInfo(waitEntryUpdateRequest, trackingCode, restaurantCode);
        return ResponseEntity.ok(waitEntryResponseDTO);
    }

    /**
     * Cancel an entry from the admin side (e.g., user left).
     */
    @DeleteMapping("/{trackingCode}")
    public ResponseEntity<Void> cancelWaitEntry(@PathVariable String restaurantCode, @PathVariable String trackingCode) {
        log.info("[ADMIN][WAITING] Cancelling wait entry ID: {}", trackingCode);
        waitEntryService.cancelWaitEntry(restaurantCode, trackingCode);
        return ResponseEntity.noContent().build();
    }

}
