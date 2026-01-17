package com.restaurant.waiting.dto.tableDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableAllocationResponse {
    private Long waitEntryId;
    private String customerName;
    private String mobile;
    private Integer tableNumber;
}

