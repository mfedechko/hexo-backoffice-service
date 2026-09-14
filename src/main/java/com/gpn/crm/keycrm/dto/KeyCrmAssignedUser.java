package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of a user assigned to an order, from GET /order?include=assigned (verified against
 * a live call). Similar to {@link KeyCrmManager} but nests a {@code role} object (instead of
 * {@code role_id}) and also carries {@code avatar}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmAssignedUser(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String email,
        String username,
        String phone,
        @JsonProperty("avatar_id") Long avatarId,
        String status,
        @JsonProperty("last_logged_at") String lastLoggedAt,
        @JsonProperty("ready_at") String readyAt,
        @JsonProperty("is_ready") Boolean isReady,
        @JsonProperty("use_2fa") Boolean use2fa,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt,
        KeyCrmRole role,
        @JsonProperty("full_name") String fullName,
        /** Always null in every entry observed so far; shape unverified. */
        Object avatar
) {
}
