package com.gpn.leads.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Runs ahead of the Spring Security filter chain (see {@link Ordered#HIGHEST_PRECEDENCE})
 * so requests are logged even when security rejects them with a 401/403.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final long startedAt = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            final long durationMs = System.currentTimeMillis() - startedAt;
            final String query = request.getQueryString();
            final String uri = query != null ? request.getRequestURI() + "?" + query : request.getRequestURI();

            log.info("{} {} -> {} ({} ms) from {}",
                    request.getMethod(),
                    uri,
                    response.getStatus(),
                    durationMs,
                    request.getRemoteAddr());
        }
    }
}
