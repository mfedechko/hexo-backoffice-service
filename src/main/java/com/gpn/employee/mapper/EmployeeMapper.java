package com.gpn.employee.mapper;

import com.gpn.employee.model.EmployeeEntity;
import com.gpn.employee.model.dto.CreateEmployeeRequest;
import com.gpn.employee.model.dto.EmployeeDto;
import com.gpn.employee.model.dto.UpdateEmployeeRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class EmployeeMapper {

    public static EmployeeDto toDto(final EmployeeEntity entity) {
        return new EmployeeDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getBirthday(),
                entity.getDepartment(),
                entity.getParentId(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static EmployeeEntity toEntity(final CreateEmployeeRequest request) {
        final var entity = new EmployeeEntity();
        applyTo(entity, request.firstName(), request.middleName(), request.lastName(),
                request.phone(), request.email(), request.birthday(), request.department(), request.parentId());
        return entity;
    }

    public static void updateEntity(final EmployeeEntity entity, final UpdateEmployeeRequest request) {
        applyTo(entity, request.firstName(), request.middleName(), request.lastName(),
                request.phone(), request.email(), request.birthday(), request.department(), request.parentId());
    }

    private static void applyTo(final EmployeeEntity entity,
                                 final String firstName,
                                 final String middleName,
                                 final String lastName,
                                 final String phone,
                                 final String email,
                                 final LocalDateTime birthday,
                                 final String department,
                                 final Long parentId) {
        entity.setFirstName(firstName);
        entity.setMiddleName(middleName);
        entity.setLastName(lastName);
        entity.setPhone(phone);
        entity.setEmail(email);
        entity.setBirthday(birthday);
        entity.setDepartment(department);
        entity.setParentId(parentId);
    }
}
