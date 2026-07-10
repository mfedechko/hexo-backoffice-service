package com.gpn.leads.controller;

import com.gpn.leads.model.dto.CreateLeadRequest;
import com.gpn.leads.model.dto.LeadResponseDto;
import com.gpn.leads.service.LeadService;
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
public class LeadController {

    private final LeadService leadService;

    /**
     * POST /api/leads
     * Called by the landing page form when a visitor submits their details.
     */
    @PostMapping
    public ResponseEntity<?> createLead(@Valid @RequestBody final CreateLeadRequest request) {
        final var lead = leadService.createNewLead(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                    "id",      lead.getId(),
                    "message", "Дякуємо! Ми зв'яжемося з вами найближчим часом."
                ));
    }

    /**
     * GET /api/leads
     * Internal endpoint — returns all submitted leads, newest first.
     * Secure this with Spring Security before going to production.
     */
    @GetMapping
    public ResponseEntity<List<LeadResponseDto>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    /**
     * GET /api/leads/{id}
     * Fetch a single lead by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeadResponseDto> getLeadById(@PathVariable Long id) {
        return ResponseEntity.ok(leadService.getLead(id));
    }
}
