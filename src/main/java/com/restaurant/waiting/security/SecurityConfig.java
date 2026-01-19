package com.restaurant.waiting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtLoggingFilter jwtLoggingFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable()).sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth ->
                auth
                        // ✅ SWAGGER (DEV ONLY)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs" +
                                "/**").permitAll()

                        // ✅ PUBLIC – CUSTOMER
                        .requestMatchers("/api/public/**").permitAll()

                        // 🔐 JWT ONLY – ONBOARDING
                        .requestMatchers(HttpMethod.POST, "/api/admin/restaurants").authenticated()

                        // 🔒 JWT + RESTAURANT
                        .requestMatchers("/api/admin/**").authenticated()

                        .anyRequest().denyAll()).oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));

        http.addFilterAfter(jwtLoggingFilter, SecurityContextHolderFilter.class);

        return http.build();
    }
}

