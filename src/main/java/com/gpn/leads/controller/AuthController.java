package com.gpn.leads.controller;

import com.gpn.leads.model.RoleEntity;
import com.gpn.leads.model.UserEntity;
import com.gpn.leads.model.dto.LoginRequest;
import com.gpn.leads.model.dto.LoginResponse;
import com.gpn.leads.repository.UserRepository;
import com.gpn.leads.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication for backoffice operations")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            final AuthenticationManager authenticationManager,
            final JwtService jwtService,
            final UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Log in",
            description = "Exchanges user credentials for a JWT to use as a Bearer token on protected endpoints."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login succeeded"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    })
    public LoginResponse login(@Valid @RequestBody final LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        final UserEntity user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        final String token = jwtService.generateToken(user);
        final var roles = user.getRoles().stream().map(RoleEntity::getName).toList();

        return new LoginResponse(token, user.getUsername(), user.getEmail(), roles);
    }
}
