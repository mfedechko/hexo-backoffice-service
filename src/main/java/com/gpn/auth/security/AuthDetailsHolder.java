package com.gpn.auth.security;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Static access point to the current request's {@link AuthDetails}, backed by
 * {@link SecurityContextHolder} — the ThreadLocal Spring Security already maintains and
 * clears per request — rather than a second, hand-rolled ThreadLocal.
 */
public final class AuthDetailsHolder {

    private AuthDetailsHolder() {
    }

    public static AuthDetails getCurrentUser() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthDetails authDetails)) {
            throw new IllegalStateException("No authenticated user in the current security context");
        }
        return authDetails;
    }
}
