package com.restaurant.waiting.model.waitEntry;

import com.restaurant.waiting.model.restaurant.Restaurant;
import com.restaurant.waiting.model.table.Table;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entity representing a customer's wait entry in the restaurant queue
 */
@Entity
@jakarta.persistence.Table(name = "wait_entry", uniqueConstraints = {@UniqueConstraint(name =
        "uq_tracking_per_restaurant", columnNames = {"restaurant_id", "tracking_code"})})
@Getter
@Setter
@NoArgsConstructor
public class WaitEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    @Column(name = "mobile", nullable = false, length = 15)
    private String mobile;

    @NotNull(message = "Party size is required")
    @Positive(message = "Party size must be positive")
    @Column(name = "party_size", nullable = false)
    private Integer partySize;

    @NotNull(message = "Ladies included flag is required")
    @Column(name = "ladies_included", nullable = false)
    private Boolean ladiesIncluded;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WaitStatus status = WaitStatus.WAITING;

    @Column(name = "joined_at", nullable = false)
    private Instant joinedAt;

    @Column(name = "notified_at")
    private Instant notifiedAt;

    @Column(name = "seated_at")
    private Instant seatedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_table_id")
    private Table assignedTable;

    @Column(name = "tracking_code", unique = true, nullable = false, updatable = false)
    private String trackingCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "completed_at")
    private Instant completedAt;

    @PrePersist
    protected void onCreate() {
        if (joinedAt == null) {
            joinedAt = Instant.now();
        }
        if (status == null) {
            status = WaitStatus.WAITING;
        }
    }

    // ===== Domain actions =====
    public void markNotified(com.restaurant.waiting.model.table.Table table) {
        if (status != WaitStatus.WAITING) {
            throw new IllegalStateException("Only WAITING entry can be notified");
        }
        validateSameRestaurant(table);
        this.status = WaitStatus.NOTIFIED;
        this.notifiedAt = Instant.now();
        this.assignedTable = table;
    }

    public void markForcedNotified(com.restaurant.waiting.model.table.Table table) {
        validateSameRestaurant(table);
        this.status = WaitStatus.NOTIFIED;
        this.notifiedAt = Instant.now();
        this.assignedTable = table;
    }

    public void markSeated() {
        if (status != WaitStatus.NOTIFIED) {
            throw new IllegalStateException("Only NOTIFIED entry can be seated");
        }
        if (assignedTable == null) {
            throw new IllegalStateException("Cannot seat without assigned table");
        }
        this.status = WaitStatus.SEATED;
        this.seatedAt = Instant.now();
    }

    public void markForcedSeated() {
        this.status = WaitStatus.SEATED;
        this.seatedAt = Instant.now();
    }

    public void markSkipped() {
        this.status = WaitStatus.SKIPPED;
    }

    public void markCompleted() {
        if (this.status != WaitStatus.SEATED) {
            throw new IllegalStateException("Only SEATED entry can be completed");
        }

        this.status = WaitStatus.COMPLETED;
        this.completedAt = Instant.now();
    }

    private void validateSameRestaurant(com.restaurant.waiting.model.table.Table table) {
        if (this.restaurant == null) {
            throw new IllegalStateException("WaitEntry is not associated with a restaurant");
        }
        if (table.getRestaurant() == null) {
            throw new IllegalStateException("Table is not associated with a restaurant");
        }
        if (!table.getRestaurant().getId().equals(this.restaurant.getId())) {
            throw new IllegalStateException("Table does not belong to this restaurant");
        }
    }

    public void forceSeat(com.restaurant.waiting.model.table.Table table) {
        if (this.status == WaitStatus.SEATED) {
            throw new IllegalStateException("Customer already seated");
        }
        if (this.status == WaitStatus.COMPLETED || this.status == WaitStatus.CANCELLED || this.status == WaitStatus.SKIPPED) {
            throw new IllegalStateException("Customer cannot be seated");
        }

        validateSameRestaurant(table);

        this.assignedTable = table;
        this.status = WaitStatus.SEATED;
        this.seatedAt = Instant.now();
    }
}