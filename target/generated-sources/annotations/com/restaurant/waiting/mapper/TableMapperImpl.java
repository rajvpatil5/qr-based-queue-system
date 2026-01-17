package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.tableDTO.TableAllocationResponse;
import com.restaurant.waiting.dto.tableDTO.TableResponse;
import com.restaurant.waiting.model.Table;
import com.restaurant.waiting.model.WaitEntry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-17T15:02:41+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class TableMapperImpl implements TableMapper {

    @Override
    public List<TableResponse> toTableResponseList(List<Table> tables) {
        if ( tables == null ) {
            return null;
        }

        List<TableResponse> list = new ArrayList<TableResponse>( tables.size() );
        for ( Table table : tables ) {
            list.add( toTableResponse( table ) );
        }

        return list;
    }

    @Override
    public TableResponse toTableResponse(Table table) {
        if ( table == null ) {
            return null;
        }

        TableResponse tableResponse = new TableResponse();

        tableResponse.setId( table.getId() );
        tableResponse.setTableNumber( table.getTableNumber() );
        tableResponse.setCapacity( table.getCapacity() );
        tableResponse.setStatus( table.getStatus() );

        return tableResponse;
    }

    @Override
    public TableAllocationResponse toTableAllocationResponse(WaitEntry entry, Table table) {
        if ( entry == null && table == null ) {
            return null;
        }

        Long waitEntryId = null;
        String customerName = null;
        String mobile = null;
        if ( entry != null ) {
            waitEntryId = entry.getId();
            customerName = entry.getName();
            mobile = entry.getMobile();
        }
        Integer tableNumber = null;
        if ( table != null ) {
            tableNumber = table.getTableNumber();
        }

        TableAllocationResponse tableAllocationResponse = new TableAllocationResponse( waitEntryId, customerName, mobile, tableNumber );

        return tableAllocationResponse;
    }
}
