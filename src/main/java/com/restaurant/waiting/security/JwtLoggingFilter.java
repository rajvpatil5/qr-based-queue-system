package com.restaurant.waiting.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Profile("dev") // 🔥 IMPORTANT
public class JwtLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Auth class = {}", auth == null ? "null" : auth.getClass().getName());
        log.info("Principal class = {}", auth == null ? "null" : auth.getPrincipal().getClass().getName());

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            String tokenValue = jwtAuth.getToken().getTokenValue();

            log.info("JWT ACCESS TOKEN: {}", tokenValue);
            log.info("JWT sub={}, roles={}",
                    jwtAuth.getToken().getSubject(),
                    jwtAuth.getAuthorities()
            );
        }
        log.info("JwtLoggingFilter::doFilterInternal ->  = {}");

        filterChain.doFilter(request, response);
    }
}

