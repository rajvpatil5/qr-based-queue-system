package com.restaurant.waiting.dto.entryDTO;

import com.restaurant.waiting.model.waitEntry.WaitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitListItemResponse {
    Long id;

    String name;

    Integer partySize;

    Boolean ladiesIncluded;

    WaitStatus status;

    Integer position;

    Integer estimatedMinutes;
}
