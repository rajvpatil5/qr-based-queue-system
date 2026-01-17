package com.restaurant.waiting.dto.tableDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableBulkCreateRequest {

    @NotNull
    private Integer startNumber;

    @NotNull
    private Integer endNumber;

    @NotNull
    private Integer capacity;

    private boolean ladiesOnly;
}
