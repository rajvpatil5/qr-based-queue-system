package com.restaurant.waiting.service;

import com.restaurant.waiting.dto.entryDTO.*;
import com.restaurant.waiting.exception.DuplicateEntryException;
import com.restaurant.waiting.exception.ResourceNotFoundException;
import com.restaurant.waiting.mapper.WaitEntryMapper;
import com.restaurant.waiting.model.*;
import com.restaurant.waiting.repository.TableRepository;
import com.restaurant.waiting.repository.WaitEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static com.restaurant.waiting.model.WaitStatus.WAITING;

/**
 * Service layer for managing wait entries and queue operations
 */
@Service
@Transactional
@Slf4j
public class WaitEntryServiceImpl implements WaitEntryService {

    private final WaitEntryRepository waitEntryRepository;
    private final WaitEntryMapper mapper;
    private final RestaurantService restaurantService;
    private final TableRepository tableRepository;

    @Value("${app.waiting.avg-time-per-table-minutes:20}")
    private int avgTimePerTableMinutes;

    @Value("${app.waiting.auto-expire-minutes:30}")
    private int autoExpireMinutes;

    public WaitEntryServiceImpl(
            WaitEntryRepository waitEntryRepository,
            WaitEntryMapper mapper, RestaurantService restaurantService, TableRepository tableRepository
    ) {
        this.waitEntryRepository = waitEntryRepository;
        this.mapper = mapper;
        this.restaurantService = restaurantService;
        this.tableRepository = tableRepository;
    }

    /* =========================================================
       PUBLIC FLOW
       ========================================================= */

    /**
     * Customer update queue information
     *
     * @return
     */
    public WaitEntryResponse updateInfo(WaitEntryUpdateRequest updateRequest, String trackingCode, String restaurantCode) {
        log.info("WaitEntryService::updateInfo -> updateRequest = {}", updateRequest);
        restaurantService.getOrThrow(restaurantCode);
        WaitEntry waitEntry = waitEntryRepository.findByTrackingCode(trackingCode).orElseThrow(() -> new RuntimeException("User not found"));

        if (waitEntry.getStatus() != WaitStatus.WAITING) {
            throw new IllegalStateException("Cannot modify after notification");
        }

        mapper.updateEntityFromRequest(updateRequest, waitEntry);
        WaitEntry updatedUser = waitEntryRepository.save(waitEntry);

        Integer position = calculatePosition(updatedUser);
        Integer eta = estimateWaitTime(position);

        return mapper.toResponse(updatedUser, position, eta);
    }

    @Override
    public StatusResponse getStatusByTrackingCode(String restaurantCode, String trackingCode) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);
        WaitEntry waitEntry = waitEntryRepository.findByRestaurantAndTrackingCode(restaurant, trackingCode).orElseThrow(() -> new RuntimeException("Invalid tracking code"));
        Integer position = null;
        Integer eta = null;
        Table table = null;


        if (waitEntry.getStatus() == WaitStatus.WAITING) {
            position = calculatePosition(waitEntry);
            eta = estimateWaitTime(position);
        }
        if (waitEntry.getStatus() == WaitStatus.NOTIFIED
                || waitEntry.getStatus() == WaitStatus.SEATED) {
            table = waitEntry.getAssignedTable();
        }
        return mapper.toStatusResponse(waitEntry, position, eta, table);
    }


    @Override
    @Transactional(readOnly = true)
    public List<WaitListItemResponse> getActiveQueue() {
        List<WaitEntry> waitingEntries = waitEntryRepository.findByStatusInOrderByJoinedAtAsc(List.of(WAITING, WaitStatus.NOTIFIED));
        List<WaitListItemResponse> waitingList = waitingEntries.stream().map(entry -> {
            Integer position = entry.getStatus() == WAITING
                    ? calculatePosition(entry)
                    : null;
            Integer eta = estimateWaitTime(position);
            WaitListItemResponse waitListItem = mapper.toWaitListItem(entry, position, eta);
            return waitListItem;
        }).toList();
        return waitingList;
    }

    /**
     * Customer joins queue (QR flow)
     */
    public WaitEntryResponse joinQueue(WaitEntryRequest request, String restaurantCode) {

        log.info("Join queue request for mobile={}", request.getMobile());

        if (waitEntryRepository.existsByMobileAndStatus(
                request.getMobile(), WAITING)) {

            throw new DuplicateEntryException(
                    "Active waiting entry already exists for this mobile"
            );
        }
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);

        WaitEntry entry = mapper.toEntity(request);
        entry.setRestaurant(restaurant);
        entry.setStatus(WAITING);
        entry.setJoinedAt(Instant.now());
        entry.setExpiresAt(
                Instant.now().plusSeconds(autoExpireMinutes * 60L)
        );
        String code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        entry.setTrackingCode(code);

        waitEntryRepository.save(entry);

        Integer position = calculatePosition(entry);
        Integer eta = estimateWaitTime(position);

        return mapper.toResponse(entry, position, eta);
    }


    /**
     * Customer cancels their own entry
     */
    public void cancelWaitEntry(String restaurantCode, String trackingCode) {
        restaurantService.getOrThrow(restaurantCode);

        WaitEntry entry = waitEntryRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Wait entry not found")
                );

        if (entry.getStatus() == WaitStatus.SEATED) {
            throw new IllegalStateException(
                    "Cannot cancel after being seated"
            );
        }

        entry.setStatus(WaitStatus.CANCELLED);
    }

    /* =========================================================
       QUEUE POSITION LOGIC
       ========================================================= */

    private Integer calculatePosition(WaitEntry entry) {

        if (entry.getStatus() != WAITING) {
            return null;
        }

        long count =
                waitEntryRepository.countWaitingBefore(entry.getJoinedAt());

        return (int) count + 1;
    }

    private Integer estimateWaitTime(Integer position) {
        return position == null ? null : position * avgTimePerTableMinutes;
    }

    public Page<CustomerResponse> getCustomersByStatus(
            String restaurantCode,
            WaitStatus status,
            int page,
            int size
    ) {
        Restaurant restaurant = restaurantService.getOrThrow(restaurantCode);

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "joinedAt")
        );

        return waitEntryRepository
                .findByRestaurantAndStatusOrderByJoinedAtAsc(
                        restaurant,
                        status,
                        pageRequest
                )
                .map(mapper::toCustomerResponse);
    }
}