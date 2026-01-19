package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.tableDTO.TableAllocationResponse;
import com.restaurant.waiting.dto.tableDTO.TableResponse;
import com.restaurant.waiting.model.table.Table;
import com.restaurant.waiting.model.waitEntry.WaitEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {
    List<TableResponse> toTableResponseList(List<Table> tables);

    TableResponse toTableResponse(Table table);

    @Mapping(target = "waitEntryId", source = "entry.id")
    @Mapping(target = "customerName", source = "entry.name")
    @Mapping(target = "mobile", source = "entry.mobile")
    @Mapping(target = "tableNumber", source = "table.tableNumber")
    TableAllocationResponse toTableAllocationResponse(WaitEntry entry, Table table);
}
