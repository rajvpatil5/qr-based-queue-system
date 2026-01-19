package com.restaurant.waiting.scheduler;

import com.restaurant.waiting.model.table.Table;
import com.restaurant.waiting.model.table.TableStatus;
import com.restaurant.waiting.model.waitEntry.WaitEntry;
import com.restaurant.waiting.repository.TableRepository;
import com.restaurant.waiting.repository.WaitEntryRepository;
import com.restaurant.waiting.service.TableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NoShowTimeoutScheduler {

    private final WaitEntryRepository waitEntryRepository;

    private final TableRepository tableRepository;

    private final TableService tableService;

    @Value("${app.waiting.no-show-timeout-minutes:5}")
    private int noShowTimeoutMinutes;

    /**
     * Runs every minute Handles customers who were notified but never showed up
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void handleNoShows() {

        Instant cutoff =
                Instant.now().minus(noShowTimeoutMinutes, ChronoUnit.MINUTES);

        List<WaitEntry> noShows =
                waitEntryRepository.findExpiredNotifications(cutoff);

        if (noShows.isEmpty()) {
            return;
        }

        log.info("No-show handler found {} expired notifications", noShows.size());

        for (WaitEntry entry : noShows) {

            Table table = entry.getAssignedTable();
            if (table == null) {
                log.warn("No-show entry {} has no assigned table", entry.getId());
                entry.markSkipped();
                continue;
            }

            // Defensive checks
            if (table.getStatus() != TableStatus.RESERVED) {
                log.warn("Table {} not RESERVED for no-show entry {}",
                        table.getId(), entry.getId());
                entry.markSkipped();
                continue;
            }

            // Core logic
            entry.markSkipped();
            table.markAvailable();

            // Immediately try to re-allocate
            tableService.allocate(
                    table.getRestaurant().getRestaurantCode(),
                    table.getId()
            );
        }
    }
}