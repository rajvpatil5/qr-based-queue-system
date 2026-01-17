package com.restaurant.waiting.dto.entryDTO;

import com.restaurant.waiting.model.WaitStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private String name;
    private String mobile;
    private Integer partySize;
    private WaitStatus status;
    private Integer tableNumber;   // nullable
    private Instant joinedAt;
    private Instant notifiedAt;
    private Instant seatedAt;
    private Instant completedAt;
}