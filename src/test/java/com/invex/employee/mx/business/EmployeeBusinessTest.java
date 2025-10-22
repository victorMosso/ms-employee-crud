package com.invex.employee.mx.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.invex.employee.mx.entities.EmployeeEntity;
import com.invex.employee.mx.exceptions.InvalidDateFormatException;
import com.invex.employee.mx.models.EmployeeDto;
import com.invex.employee.mx.repositories.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeBusinessTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeBusiness employeeBusiness;

    private EmployeeEntity employeeEntity;
    private EmployeeDto employeeDto;
    
    private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter CREATED_AT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy:HH:mm:ss");

    @BeforeEach
    void setUp() {
        employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setFirstName("John");
        employeeEntity.setSecondName("Doe");
        employeeEntity.setPaternalSurname("Smith");
        employeeEntity.setMaternalSurname("Jones");
        employeeEntity.setAge(30);
        employeeEntity.setGender('M');
        employeeEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        employeeEntity.setPosition("Developer");
        employeeEntity.setCreatedAt(LocalDateTime.now());
        employeeEntity.setIsActive(true);

        employeeDto = new EmployeeDto(
            1L,
            "John",
            "Doe",
            "Smith",
            "Jones",
            30,
            'M',
            employeeEntity.getBirthDate().format(BIRTH_DATE_FORMATTER),
            "Developer",
            employeeEntity.getCreatedAt().format(CREATED_AT_FORMATTER),
            true
        );
    }

    @Test
    void testFindAll() {
        // Given
        when(employeeRepository.findAll()).thenReturn(List.of(employeeEntity));

        // When
        List<EmployeeDto> result = employeeBusiness.findAll();

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(employeeDto.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    void testFindAllEmpty() {
        // Given
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<EmployeeDto> result = employeeBusiness.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindById() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeEntity));

        // When
        EmployeeDto result = employeeBusiness.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(employeeDto.getId(), result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        EmployeeDto result = employeeBusiness.findById(1L);

        // Then
        assertNull(result);
    }

    @Test
    void testFindByName() {
        // Given
        when(employeeRepository.findByFirstNameContainingIgnoreCase("John")).thenReturn(List.of(employeeEntity));

        // When
        List<EmployeeDto> result = employeeBusiness.findByName("John");

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testFindByNameEmpty() {
        // Given
        when(employeeRepository.findByFirstNameContainingIgnoreCase(anyString())).thenReturn(Collections.emptyList());

        // When
        List<EmployeeDto> result = employeeBusiness.findByName("Unknown");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateEmployees() {
        // Given
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        // When
        List<EmployeeDto> result = employeeBusiness.createEmployees(List.of(employeeDto));

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testCreateEmployeesEmptyList() {
        // When
        List<EmployeeDto> result = employeeBusiness.createEmployees(Collections.emptyList());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(0)).save(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployee() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);

        // When
        EmployeeDto result = employeeBusiness.updateEmployee(1L, employeeDto);

        // Then
        assertNotNull(result);
        assertEquals(employeeDto.getId(), result.getId());
        verify(employeeRepository, times(1)).save(any(EmployeeEntity.class));
    }

    @Test
    void testUpdateEmployeeNotFound() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        EmployeeDto result = employeeBusiness.updateEmployee(1L, employeeDto);

        // Then
        assertNull(result);
        verify(employeeRepository, times(0)).save(any(EmployeeEntity.class));
    }

    @Test
    void testDeleteEmployee() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        // When
        boolean result = employeeBusiness.deleteEmployee(1L);

        // Then
        assertTrue(result);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        // Given
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // When
        boolean result = employeeBusiness.deleteEmployee(1L);

        // Then
        assertFalse(result);
        verify(employeeRepository, times(0)).deleteById(anyLong());
    }
    
    @Test
    void testCreateEmployeesWithInvalidDateFormat() {
        // Given
        employeeDto.setBirthDate("1990-01-01"); // Invalid format

        // When & Then
        assertThrows(InvalidDateFormatException.class, () -> {
            employeeBusiness.createEmployees(List.of(employeeDto));
        });
    }
    
    @Test
    void testUpdateEmployeeWithInvalidDateFormat() {
        // Given
        employeeDto.setBirthDate("1990-01-01"); // Invalid format
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeEntity));

        // When & Then
        assertThrows(InvalidDateFormatException.class, () -> {
            employeeBusiness.updateEmployee(1L, employeeDto);
        });
    }

    @Test
    void testConvertEntityToDtoWithInvalidDateFormat() {
        // Given
        employeeEntity.setBirthDate(null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeEntity));

        // When & Then
        assertThrows(InvalidDateFormatException.class, () -> {
            employeeBusiness.findById(1L);
        });
    }
}
