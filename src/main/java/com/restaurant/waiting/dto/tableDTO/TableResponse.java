package com.restaurant.waiting.dto.tableDTO;

import com.restaurant.waiting.model.table.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {
    Long id;

    Integer tableNumber;

    Integer capacity;

    TableStatus status;

    boolean ladiesOnly;
}
