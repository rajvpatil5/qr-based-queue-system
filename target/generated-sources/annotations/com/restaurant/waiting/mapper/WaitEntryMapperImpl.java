package com.restaurant.waiting.mapper;

import com.restaurant.waiting.dto.entryDTO.CustomerResponse;
import com.restaurant.waiting.dto.entryDTO.StatusResponse;
import com.restaurant.waiting.dto.entryDTO.WaitEntryRequest;
import com.restaurant.waiting.dto.entryDTO.WaitEntryResponse;
import com.restaurant.waiting.dto.entryDTO.WaitEntryUpdateRequest;
import com.restaurant.waiting.dto.entryDTO.WaitListItemResponse;
import com.restaurant.waiting.model.Table;
import com.restaurant.waiting.model.WaitEntry;
import com.restaurant.waiting.model.WaitStatus;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-17T19:53:10+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class WaitEntryMapperImpl implements WaitEntryMapper {

    @Override
    public WaitEntry toEntity(WaitEntryRequest request) {
        if ( request == null ) {
            return null;
        }

        WaitEntry waitEntry = new WaitEntry();

        waitEntry.setName( request.getName() );
        waitEntry.setMobile( request.getMobile() );
        waitEntry.setPartySize( request.getPartySize() );
        waitEntry.setLadiesIncluded( request.getLadiesIncluded() );

        return waitEntry;
    }

    @Override
    public WaitEntryResponse toResponse(WaitEntry entry, Integer position, Integer estimatedMinutes) {
        if ( entry == null && position == null && estimatedMinutes == null ) {
            return null;
        }

        WaitEntryResponse waitEntryResponse = new WaitEntryResponse();

        if ( entry != null ) {
            waitEntryResponse.setId( entry.getId() );
            waitEntryResponse.setName( entry.getName() );
            waitEntryResponse.setMobile( entry.getMobile() );
            waitEntryResponse.setPartySize( entry.getPartySize() );
            waitEntryResponse.setLadiesIncluded( entry.getLadiesIncluded() );
            waitEntryResponse.setStatus( entry.getStatus() );
            waitEntryResponse.setJoinedAt( entry.getJoinedAt() );
        }
        waitEntryResponse.setPosition( position );
        waitEntryResponse.setEstimatedMinutes( estimatedMinutes );

        return waitEntryResponse;
    }

    @Override
    public void updateEntityFromRequest(WaitEntryUpdateRequest request, WaitEntry entity) {
        if ( request == null ) {
            return;
        }

        entity.setName( request.getName() );
        entity.setPartySize( request.getPartySize() );
        entity.setLadiesIncluded( request.getLadiesIncluded() );
    }

    @Override
    public WaitListItemResponse toWaitListItem(WaitEntry entry, Integer position, Integer estimatedMinutes) {
        if ( entry == null && position == null && estimatedMinutes == null ) {
            return null;
        }

        WaitListItemResponse waitListItemResponse = new WaitListItemResponse();

        if ( entry != null ) {
            waitListItemResponse.setId( entry.getId() );
            waitListItemResponse.setName( entry.getName() );
            waitListItemResponse.setPartySize( entry.getPartySize() );
            waitListItemResponse.setLadiesIncluded( entry.getLadiesIncluded() );
            waitListItemResponse.setStatus( entry.getStatus() );
        }
        waitListItemResponse.setPosition( position );
        waitListItemResponse.setEstimatedMinutes( estimatedMinutes );

        return waitListItemResponse;
    }

    @Override
    public StatusResponse toStatusResponse(WaitEntry entry, Integer position, Integer estimatedMinutes, Table table) {
        if ( entry == null && position == null && estimatedMinutes == null && table == null ) {
            return null;
        }

        StatusResponse statusResponse = new StatusResponse();

        if ( entry != null ) {
            statusResponse.setId( entry.getId() );
            statusResponse.setStatus( entry.getStatus() );
        }
        statusResponse.setPosition( position );
        statusResponse.setEstimatedMinutes( estimatedMinutes );
        statusResponse.setTableNumber( table != null ? table.getTableNumber() : null );
        statusResponse.setMessage( buildMessage(entry) );

        return statusResponse;
    }

    @Override
    public CustomerResponse toCustomerResponse(WaitEntry entry) {
        if ( entry == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String mobile = null;
        Integer partySize = null;
        WaitStatus status = null;
        Instant joinedAt = null;
        Instant notifiedAt = null;
        Instant seatedAt = null;
        Instant completedAt = null;

        id = entry.getId();
        name = entry.getName();
        mobile = entry.getMobile();
        partySize = entry.getPartySize();
        status = entry.getStatus();
        joinedAt = entry.getJoinedAt();
        notifiedAt = entry.getNotifiedAt();
        seatedAt = entry.getSeatedAt();
        completedAt = entry.getCompletedAt();

        Integer tableNumber = entry.getAssignedTable() != null ? entry.getAssignedTable().getTableNumber() : null;

        CustomerResponse customerResponse = new CustomerResponse( id, name, mobile, partySize, status, tableNumber, joinedAt, notifiedAt, seatedAt, completedAt );

        return customerResponse;
    }
}
