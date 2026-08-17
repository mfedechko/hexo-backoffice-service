package com.gpn.employee.repository;

import com.gpn.employee.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long>,
        JpaSpecificationExecutor<EmployeeEntity> {

}
