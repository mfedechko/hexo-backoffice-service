package com.gpn.leads.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "A lead submitted through the landing page")
public class LeadResponseDto {

    @Schema(description = "Lead ID", example = "1")
    private Long id;

    @Schema(description = "Full name of the lead", example = "Jane Doe")
    private String name;

    @Schema(description = "Contact phone number", example = "+380501234567")
    private String phone;

    @Schema(description = "Optional free-text comment left by the lead", nullable = true)
    private String comment;

    @Schema(description = "Lead status", example = "CREATED", allowableValues = {"CREATED", "PROCESSED", "DECLINED"})
    private String status;

    @Schema(description = "Timestamp the lead was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp the lead was last updated", nullable = true)
    private LocalDateTime updatedAt;
}
