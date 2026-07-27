package com.gpn.leads.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Details submitted through the landing page contact form")
public class CreateLeadRequest {

    @Schema(description = "Full name of the lead", example = "Jane Doe", maxLength = 100)
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be 100 characters or fewer")
    private String name;

    @Schema(description = "Contact phone number", example = "+380501234567")
    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^\\+?[0-9\\s\\-()]{7,20}$",
            message = "Invalid phone number format"
    )
    private String phone;

    @Schema(description = "Optional free-text comment left by the lead", nullable = true)
    private String comment;

}
