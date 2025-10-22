package com.invex.employee.mx.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invex.employee.mx.entities.EmployeeEntity;
import com.invex.employee.mx.exceptions.InvalidDateFormatException;
import com.invex.employee.mx.models.EmployeeDto;
import com.invex.employee.mx.repositories.EmployeeRepository;
import com.invex.employee.mx.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeBusiness implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Logger log = LoggerFactory.getLogger(EmployeeBusiness.class);
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter CREATED_AT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss");

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findAll() {        
        List<EmployeeEntity> listEntity = employeeRepository.findAll();
        if (listEntity.isEmpty()) {
            return List.of();
        }
        // Conversion from Entity to DTO
        List<EmployeeDto> listDto = listEntity.stream().map(entity -> {
            return this.convertEntityToDto(entity);
        }).toList();
        return listDto;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto findById(Long id) {
        
        Optional<EmployeeEntity> entity = employeeRepository.findById(id);
        if(entity.isPresent()) {
            return this.convertEntityToDto(entity.get());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> findByName(String name) {

        List<EmployeeEntity> employees = employeeRepository.findByFirstNameContainingIgnoreCase(name);
        if (employees.isEmpty()) {
            return List.of();
        }
        
        List<EmployeeDto> listDto = employees.stream().map(entity -> {
            return this.convertEntityToDto(entity);
        }).toList();
        return listDto;
    }

    @Override
    @Transactional
    public List<EmployeeDto> createEmployees(List<EmployeeDto> employeeDtoList) {

        if (employeeDtoList.isEmpty()){
            return List.of();
        }
        
        employeeDtoList.stream().map(employeeDto -> {
            EmployeeEntity entity = convertDtoToEntity(employeeDto, new EmployeeEntity());
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        }).forEach(employeeRepository::save);

        return employeeDtoList;
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        Optional<EmployeeEntity> entity = employeeRepository.findById(id);
        if(entity.isPresent()) {
            employeeRepository.save(convertDtoToEntity(employeeDto, entity.get()));
            return employeeDto;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long id) {

        if(employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private EmployeeDto convertEntityToDto(EmployeeEntity entity) {
        try {
        return new EmployeeDto(
            entity.getId(),
            entity.getFirstName(),
            entity.getSecondName(),
            entity.getPaternalSurname(),
            entity.getMaternalSurname(),
            entity.getAge(),
            entity.getGender(),
            entity.getBirthDate().format(BIRTH_DATE_FORMATTER),
            entity.getPosition(),
            entity.getCreatedAt().format(CREATED_AT_FORMATTER),
            entity.getIsActive()
        );
        } catch (DateTimeParseException ex) {
            log.info("Error parsing date '{}', {}", entity.getBirthDate().toString(), ex.getMessage());
            throw new InvalidDateFormatException(entity.getBirthDate().toString(), ex);
        }
    }

    private EmployeeEntity convertDtoToEntity(EmployeeDto employeeDto, 
            EmployeeEntity empEntity) {

        empEntity.setFirstName(employeeDto.getFirstName());
        empEntity.setSecondName(employeeDto.getSecondName());
        empEntity.setPaternalSurname(employeeDto.getPaternalSurname());
        empEntity.setMaternalSurname(employeeDto.getMaternalSurname());
        empEntity.setAge(employeeDto.getAge());
        empEntity.setGender(employeeDto.getGender());
        empEntity.setPosition(employeeDto.getPosition());
        empEntity.setIsActive(employeeDto.getIsActive());
        try {
            empEntity.setBirthDate(LocalDate.parse(employeeDto.getBirthDate(), BIRTH_DATE_FORMATTER));
        } catch (DateTimeParseException ex) {
            log.info("Error parsing date '{}', {}", employeeDto.getBirthDate(), ex.getMessage());
            throw new InvalidDateFormatException(employeeDto.getBirthDate(), ex);
        }
        return empEntity;
    }
}
