package com.invex.employee.mx.services;

import java.util.List;

import com.invex.employee.mx.models.EmployeeDto;

public interface EmployeeService {
  
    List<EmployeeDto> findAll();
    
    EmployeeDto findById(Long id);
    
    List<EmployeeDto> findByName(String name);
    
    List<EmployeeDto> createEmployees(List<EmployeeDto> employeeDtoList);
    
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
    
    boolean deleteEmployee(Long id);
}


