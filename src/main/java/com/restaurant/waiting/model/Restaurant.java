package com.restaurant.waiting.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, updatable = false, length = 10)
    private String restaurantCode;   // PUBLIC identifier (SAAS KEY)

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.restaurantCode == null) {
            // 8-char uppercase code
            this.restaurantCode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        }
    }
}
