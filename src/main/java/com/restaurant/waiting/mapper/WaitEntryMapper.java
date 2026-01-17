package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.entryDTO.*;
import com.restaurant.waiting.model.Table;
import com.restaurant.waiting.model.WaitEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WaitEntryMapper {

    /* =========================================================
       REQUEST → ENTITY
       ========================================================= */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "notifiedAt", ignore = true)
    @Mapping(target = "seatedAt", ignore = true)
    @Mapping(target = "assignedTable", ignore = true)
    @Mapping(target = "trackingCode", ignore = true)
    WaitEntry toEntity(WaitEntryRequest request);

    /* =========================================================
       ENTITY → RESPONSE (JOIN QUEUE RESPONSE)
       ========================================================= */

    @Mapping(target = "position", source = "position")
    @Mapping(target = "estimatedMinutes", source = "estimatedMinutes")
    WaitEntryResponse toResponse(
            WaitEntry entry,
            Integer position,
            Integer estimatedMinutes
    );

    //    Update request to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    @Mapping(target = "notifiedAt", ignore = true)
    @Mapping(target = "seatedAt", ignore = true)
    @Mapping(target = "assignedTable", ignore = true)
    @Mapping(target = "trackingCode", ignore = true)
    void updateEntityFromRequest(WaitEntryUpdateRequest request, @MappingTarget WaitEntry entity);


    WaitListItemResponse toWaitListItem(
            WaitEntry entry,
            Integer position,
            Integer estimatedMinutes
    );

    @Mapping(target = "id", source = "entry.id")
    @Mapping(target = "status", source = "entry.status")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "estimatedMinutes", source = "estimatedMinutes")
    @Mapping(target = "tableNumber", expression = "java(table != null ? table.getTableNumber() : null)")
    @Mapping(target = "message", expression = "java(buildMessage(entry))")
    StatusResponse toStatusResponse(
            WaitEntry entry,
            Integer position,
            Integer estimatedMinutes,
            Table table
    );

    default String buildMessage(WaitEntry entry) {
        return switch (entry.getStatus()) {
            case WAITING -> "You are in the queue. Please wait.";
            case NOTIFIED -> "Your table is ready. Please arrive within 5 minutes.";
            case SEATED -> "You are seated. Enjoy your meal.";
            case SKIPPED -> "You missed your call. Please rejoin the queue.";
            case CANCELLED -> "Your waiting request was cancelled.";
            case COMPLETED -> "We hope you enjoyed your meal. Thanks for visiting us.";
        };
    }

    /* =========================================================
   ENTITY → ADMIN CUSTOMER RESPONSE
   ========================================================= */

    @Mapping(target = "id", source = "entry.id")
    @Mapping(target = "name", source = "entry.name")
    @Mapping(target = "mobile", source = "entry.mobile")
    @Mapping(target = "partySize", source = "entry.partySize")
    @Mapping(target = "status", source = "entry.status")
    @Mapping(
            target = "tableNumber",
            expression = "java(entry.getAssignedTable() != null ? entry.getAssignedTable().getTableNumber() : null)"
    )
    @Mapping(target = "joinedAt", source = "entry.joinedAt")
    @Mapping(target = "notifiedAt", source = "entry.notifiedAt")
    @Mapping(target = "seatedAt", source = "entry.seatedAt")
    @Mapping(target = "completedAt", source = "entry.completedAt")
    CustomerResponse toCustomerResponse(WaitEntry entry);
}
