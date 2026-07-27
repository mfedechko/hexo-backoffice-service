package com.gpn.auth.security;

import com.gpn.auth.model.PermissionEntity;
import com.gpn.auth.model.RoleEntity;
import com.gpn.auth.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_DEPARTMENT_ID = "departmentId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_PERMISSIONS = "permissions";

    private final SecretKey signingKey;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.security.jwt.secret}") final String secret,
            @Value("${app.security.jwt.expiration-minutes}") final long expirationMinutes) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(final UserEntity user) {
        final Instant now = Instant.now();
        final List<String> roles = user.getRoles().stream()
                .map(RoleEntity::getName)
                .distinct()
                .toList();
        final List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionEntity::getName)
                .distinct()
                .toList();

        final var builder = Jwts.builder()
                .subject(user.getUsername())
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLES, roles)
                .claim(CLAIM_PERMISSIONS, permissions)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(signingKey);

        if (user.getDepartmentId() != null) {
            builder.claim(CLAIM_DEPARTMENT_ID, user.getDepartmentId());
        }

        return builder.compact();
    }

    @SuppressWarnings("unchecked")
    public AuthDetails extractAuthDetails(final String token) {
        final Claims claims = parseClaims(token);
        final List<String> roles = claims.get(CLAIM_ROLES, List.class);
        final List<String> permissions = claims.get(CLAIM_PERMISSIONS, List.class);

        return new AuthDetails(
                toLong(claims.get(CLAIM_USER_ID)),
                claims.getSubject(),
                claims.get(CLAIM_EMAIL, String.class),
                toLong(claims.get(CLAIM_DEPARTMENT_ID)),
                Set.copyOf(Optional.ofNullable(roles).orElse(List.of())),
                Set.copyOf(Optional.ofNullable(permissions).orElse(List.of())));
    }

    private static Long toLong(final Object claim) {
        return claim == null ? null : ((Number) claim).longValue();
    }

    public boolean isTokenValid(final String token) {
        try {
            final Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(final String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
