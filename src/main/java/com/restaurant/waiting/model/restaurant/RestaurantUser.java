package com.restaurant.waiting.model.restaurant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "restaurant_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_restaurant_user_keycloak",
                        columnNames = "keycloakUserId"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RestaurantUser {

    //    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private final String role = "OWNER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Keycloak user id (JWT sub)
    @Column(nullable = false, unique = true)
    private String keycloakUserId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}

