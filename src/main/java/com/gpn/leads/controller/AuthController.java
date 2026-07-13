package com.gpn.leads.controller;

import com.gpn.leads.model.dto.LoginRequest;
import com.gpn.leads.model.dto.LoginResponse;
import com.gpn.leads.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication for backoffice operations")
public class AuthController {

    private final String adminUsername;
    private final String adminPasswordHash;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            @Value("${app.security.admin.username}") final String adminUsername,
            @Value("${app.security.admin.password-hash}") final String adminPasswordHash,
            final PasswordEncoder passwordEncoder,
            final JwtService jwtService) {
        this.adminUsername = adminUsername;
        this.adminPasswordHash = adminPasswordHash;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Log in",
            description = "Exchanges admin credentials for a JWT to use as a Bearer token on protected endpoints."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login succeeded"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public LoginResponse login(@Valid @RequestBody final LoginRequest request) {
        if (!adminUsername.equals(request.getUsername())
                || !passwordEncoder.matches(request.getPassword(), adminPasswordHash)) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new LoginResponse(jwtService.generateToken(adminUsername));
    }
}
