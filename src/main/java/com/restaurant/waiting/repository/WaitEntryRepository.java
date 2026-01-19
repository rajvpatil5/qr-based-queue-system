package com.restaurant.waiting.repository;

import com.restaurant.waiting.model.restaurant.Restaurant;
import com.restaurant.waiting.model.table.Table;
import com.restaurant.waiting.model.waitEntry.WaitEntry;
import com.restaurant.waiting.model.waitEntry.WaitStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitEntryRepository extends JpaRepository<WaitEntry, Long> {
    Optional<WaitEntry> findByRestaurantAndTrackingCode(Restaurant restaurant, String trackingCode);

    List<WaitEntry> findByStatusInOrderByJoinedAtAsc(List<WaitStatus> waitStatuses);

    Optional<WaitEntry> findByTrackingCode(String trackingCode);

    Optional<WaitEntry> findByRestaurantAndAssignedTableAndStatus(
            Restaurant restaurant,
            Table table,
            WaitStatus status
    );

    // To fetch all customer by there status
    Page<WaitEntry> findByRestaurantAndStatusOrderByJoinedAtAsc(
            Restaurant restaurant,
            WaitStatus status,
            Pageable pageable
    );

    /**
     * Find the next eligible waiting entry for a table. - FIFO (joinedAt ASC) - Capacity respected - Ladies-only
     * enforced when required - Row is pessimistically locked to avoid double notification
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT w FROM WaitEntry w
                WHERE w.restaurant = :restaurant
                  AND w.status = 'WAITING'
                  AND w.partySize <= :capacity
                  AND (:familyOnly = false OR w.ladiesIncluded = true)
                ORDER BY w.joinedAt ASC
            """)
    List<WaitEntry> findNextEligibleForTable(
            @Param("restaurant") Restaurant restaurant,
            @Param("capacity") int capacity,
            @Param("familyOnly") boolean familyOnly, Pageable pageable
    );

    /**
     * Prevent duplicate active waiting entries per mobile number
     */
    boolean existsByMobileAndStatus(String mobile, WaitStatus status);

    /**
     * Count how many WAITING entries joined before this entry. Used to calculate queue position.
     */
    @Query("""
                SELECT COUNT(w)
                FROM WaitEntry w
                WHERE w.status = 'WAITING'
                  AND w.joinedAt < :joinedAt
            """)
    long countWaitingBefore(@Param("joinedAt") Instant joinedAt);

    /**
     * Auto-skip notified entries that never arrived (no-show)
     */
    @Query("""
                SELECT w FROM WaitEntry w
                WHERE w.status = 'NOTIFIED'
                  AND w.notifiedAt < :cutoff
            """)
    List<WaitEntry> findExpiredNotifications(@Param("cutoff") Instant cutoff);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT w FROM WaitEntry w
                WHERE w.status = 'NOTIFIED'
                  AND w.assignedTable.id = :tableId
                  AND w.restaurant.id = :restaurantId
            """)
    Optional<WaitEntry> findNotifiedForTable(
            @Param("restaurantId") Long restaurantId,
            @Param("tableId") Long tableId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                select w from WaitEntry w
                where w.assignedTable = :table
                  and w.status = 'SEATED'
            """)
    Optional<WaitEntry> findSeatedByTable(@Param("table") Table table);

}
