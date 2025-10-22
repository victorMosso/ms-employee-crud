package com.invex.employee.mx.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.invex.employee.mx.entities.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByFirstNameContainingIgnoreCase(String name);

}
