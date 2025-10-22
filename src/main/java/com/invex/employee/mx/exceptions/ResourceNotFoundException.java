package com.invex.employee.mx.exceptions;

public class ResourceNotFoundException extends ApiException {
    private static final String ERROR_CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String resource, Object id) {
        super(String.format("%s con id '%s' no encontrado.", resource, id), ERROR_CODE);
    }
}
