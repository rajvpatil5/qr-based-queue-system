package com.restaurant.waiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for Restaurant Waiting Service
 */
@SpringBootApplication
@EnableScheduling
public class RestaurantWaitingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantWaitingApplication.class, args);
    }
}
