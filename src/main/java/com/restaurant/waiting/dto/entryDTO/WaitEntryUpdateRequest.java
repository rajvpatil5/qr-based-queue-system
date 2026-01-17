package com.restaurant.waiting.dto.entryDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for joining the waiting queue
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitEntryUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Party size is required")
    @Positive(message = "Party size must be positive")
    private Integer partySize;

    @NotNull(message = "Ladies included flag is required")
    private Boolean ladiesIncluded;
}
