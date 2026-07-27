package com.gpn.auth.security;

import java.util.Set;

/**
 * Full identity of the logged-in user, decoded from the JWT claims on each request
 * (no DB lookup — see JwtService/JwtAuthenticationFilter) and carried as the
 * Authentication principal for the lifetime of the request.
 */
public record AuthDetails(
        Long id,
        String username,
        String email,
        Long departmentId,
        Set<String> roles,
        Set<String> permissions) {
}
