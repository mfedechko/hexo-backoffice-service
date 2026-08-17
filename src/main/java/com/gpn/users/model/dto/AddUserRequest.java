package com.gpn.users.model.dto;

import java.time.LocalDate;

public record AddUserRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        LocalDate dateOfBirth
) {
}
