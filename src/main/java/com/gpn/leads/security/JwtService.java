package com.gpn.leads.security;

import com.gpn.leads.model.PermissionEntity;
import com.gpn.leads.model.RoleEntity;
import com.gpn.leads.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtService {

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

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(CLAIM_ROLES, roles)
                .claim(CLAIM_PERMISSIONS, permissions)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(final String token) {
        return parseClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> extractAuthorities(final String token) {
        final Claims claims = parseClaims(token);
        final List<String> roles = claims.get(CLAIM_ROLES, List.class);
        final List<String> permissions = claims.get(CLAIM_PERMISSIONS, List.class);

        return Stream.concat(
                        Optional.ofNullable(roles).orElse(List.of()).stream(),
                        Optional.ofNullable(permissions).orElse(List.of()).stream())
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
