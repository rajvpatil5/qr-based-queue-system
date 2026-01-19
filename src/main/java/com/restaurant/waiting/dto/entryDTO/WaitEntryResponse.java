package com.restaurant.waiting.dto.entryDTO;

import com.restaurant.waiting.model.waitEntry.WaitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for wait entry response after joining the queue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitEntryResponse {

    private Long id;

    private String name;

    private String mobile;

    private Integer partySize;

    private Boolean ladiesIncluded;

    private WaitStatus status;

    private Instant joinedAt;

    private Integer position;

    private Integer estimatedMinutes;
}
