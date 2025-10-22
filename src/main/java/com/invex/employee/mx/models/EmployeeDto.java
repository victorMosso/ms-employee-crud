package com.invex.employee.mx.models;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(required = false)
    private Long id;

    @JsonProperty(required = true)
    @NotBlank(message = "firstName can not be empty")
    private String firstName;
    
    @JsonProperty(required = false)
    private String secondName;

    @JsonProperty(required = true)
    @NotBlank(message = "paternalSurname can not be empty")
    private String paternalSurname;
    
    @JsonProperty(required = false)
    private String maternalSurname;

    @JsonProperty(required = true)
    @NotBlank(message = "age can not be empty")
    private Integer age;
    
    @JsonProperty(required = true)
    @NotBlank(message = "gender can not be empty")
    private Character gender;
    
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotBlank(message = "birthDate can not be empty")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "birthDate must be in the format dd-MM-yyyy")
    private String birthDate;
    
    @JsonProperty(required = true)
    @NotBlank(message = "position can not be empty")
    private String position;
    
    @JsonProperty(required = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy:HH:mm:ss")
    private String createdAt;
    
    @JsonProperty(required = true, defaultValue = "true")
    @NotBlank(message = "isActive can not be empty")
    private Boolean isActive;
}
