package com.gpn.leads.controller;

import com.gpn.leads.model.dto.CreateLeadRequest;
import com.gpn.leads.model.dto.LeadResponseDto;
import com.gpn.leads.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leads")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
@AllArgsConstructor
@Tag(name = "Leads", description = "Leads submitted through the landing page contact form")
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    @Operation(
            summary = "Submit a new lead",
            description = "Called by the landing page form when a visitor submits their details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lead created"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    public ResponseEntity<?> createLead(@Valid @RequestBody final CreateLeadRequest request) {
        final var lead = leadService.createNewLead(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                    "id",      lead.getId(),
                    "message", "Дякуємо! Ми зв'яжемося з вами найближчим часом."
                ));
    }

    @GetMapping
    @Operation(
            summary = "List all leads",
            description = "Internal endpoint — returns all submitted leads, newest first. "
                    + "Secure this with Spring Security before going to production."
    )
    @ApiResponse(responseCode = "200", description = "Leads returned")
    public ResponseEntity<List<LeadResponseDto>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lead by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lead found"),
            @ApiResponse(responseCode = "404", description = "Lead not found", content = @Content)
    })
    public ResponseEntity<LeadResponseDto> getLeadById(
            @Parameter(description = "Lead ID") @PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLead(id));
    }
}
