package com.restaurant.waiting.dto.entryDTO;

import com.restaurant.waiting.model.WaitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for wait status response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {

    private Long id;
    private WaitStatus status;
    private Integer position;
    private Integer estimatedMinutes;
    private Integer tableNumber;   // ✅ SIMPLE, SAFE
    private String message;
}
