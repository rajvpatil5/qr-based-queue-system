package com.restaurant.waiting.model.table;

import com.restaurant.waiting.model.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@jakarta.persistence.Table(name = "table_info",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_table_number_per_restaurant",
                        columnNames = {"restaurant_id", "table_number"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Floor-coded numbers: 1-99 = G, 101-199 = 1st, etc.
    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TableStatus status = TableStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean familyOnly = false;

    @Column(nullable = false)
    private Integer floor = 0; // 0 = ground

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // ========== Domain logic ==========
    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }

    public void markAvailable() {
        this.status = TableStatus.AVAILABLE;
    }

    public void markReserved() {
        if (this.status != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table must be AVAILABLE to reserve");
        }
        this.status = TableStatus.RESERVED;
    }

    public void markOccupied() {
        if (this.status != TableStatus.RESERVED) {
            throw new IllegalStateException("Table must be RESERVED to occupy");
        }
        this.status = TableStatus.OCCUPIED;
    }

}
