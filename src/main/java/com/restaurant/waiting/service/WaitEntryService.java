package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.entryDTO.*;
import com.restaurant.waiting.model.waitEntry.WaitStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WaitEntryService {
    List<WaitListItemResponse> getActiveQueue();

    WaitEntryResponse joinQueue(WaitEntryRequest request, String restaurantCode);

    WaitEntryResponse updateInfo(WaitEntryUpdateRequest updateRequest, String trackingCode, String restaurantCode);

    StatusResponse getStatusByTrackingCode(String restaurantCode, String trackingCode);

    Page<CustomerResponse> getCustomersByStatus(String restaurantCode, WaitStatus status, int page, int size);

    void cancelWaitEntry(String restaurantCode, String trackingCode);
}
