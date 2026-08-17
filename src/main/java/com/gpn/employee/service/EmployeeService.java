package com.gpn.employee.service;

import com.gpn.employee.exception.EmployeeNotFoundException;
import com.gpn.employee.mapper.EmployeeMapper;
import com.gpn.employee.model.EmployeeEntity;
import com.gpn.employee.model.EmployeeStatus;
import com.gpn.employee.model.dto.CreateEmployeeRequest;
import com.gpn.employee.model.dto.EmployeeDto;
import com.gpn.employee.model.dto.UpdateEmployeeRequest;
import com.gpn.employee.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(EmployeeMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployee(final Long id) {
        return EmployeeMapper.toDto(findEmployeeOrThrow(id));
    }

    @Transactional
    public EmployeeDto addEmployee(final CreateEmployeeRequest request) {
        final var entity = EmployeeMapper.toEntity(request);
        entity.setStatus(EmployeeStatus.ACTIVE);
        final var saved = employeeRepository.save(entity);
        return EmployeeMapper.toDto(saved);
    }

    @Transactional
    public EmployeeDto modifyEmployee(final Long id, final UpdateEmployeeRequest request) {
        final var employee = findEmployeeOrThrow(id);
        EmployeeMapper.updateEntity(employee, request);
        final var saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    @Transactional
    public EmployeeDto suspendEmployee(final Long id) {
        final var employee = findEmployeeOrThrow(id);
        employee.setStatus(EmployeeStatus.SUSPENDED);
        final var saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    @Transactional
    public EmployeeDto reactivateEmployee(final Long id) {
        final var employee = findEmployeeOrThrow(id);
        employee.setStatus(EmployeeStatus.ACTIVE);
        final var saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

    private EmployeeEntity findEmployeeOrThrow(final Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }
}
