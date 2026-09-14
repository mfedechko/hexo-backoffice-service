package com.gpn.crm.keycrm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw shape of the manager assigned to an order, from GET /order?include=manager (verified
 * against a live call).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record KeyCrmManager(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String email,
        String username,
        String phone,
        @JsonProperty("role_id") Long roleId,
        @JsonProperty("avatar_id") Long avatarId,
        String status,
        @JsonProperty("last_logged_at") String lastLoggedAt,
        @JsonProperty("ready_at") String readyAt,
        @JsonProperty("is_ready") Boolean isReady,
        @JsonProperty("use_2fa") Boolean use2fa,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("deleted_at") String deletedAt,
        @JsonProperty("full_name") String fullName
) {
}
