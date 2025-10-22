package com.invex.employee.mx.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Setter
@Getter
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "second_name")
    private String secondName;

    @Column(name = "paternal_surname")
    private String paternalSurname;

    @Column(name = "maternal_surname")
    private String maternalSurname;

    @Column(columnDefinition = "INT(3)")
    private Integer age;

    @Column(columnDefinition = "CHAR(1)")
    private Character gender;

    @Column(columnDefinition = "DATE", name = "birth_date")
    private LocalDate birthDate;

    private String position;

    @Column(name = "date_registration_system", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TINYINT(1)", name = "state")
    private Boolean isActive;
}