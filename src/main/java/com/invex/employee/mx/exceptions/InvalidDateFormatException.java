package com.invex.employee.mx.exceptions;

public class InvalidDateFormatException extends ApiException {
    private static final String ERROR_CODE = "INVALID_DATE_FORMAT";

    public InvalidDateFormatException(String message) {
        super("Fecha inválida: '" + message + "'. Se esperaba el formato dd-MM-yyyy.", ERROR_CODE);
    }

    public InvalidDateFormatException(String message, Throwable cause) {
        super("Fecha inválida: '" + message + "'. Se esperaba el formato dd-MM-yyyy.", ERROR_CODE, cause);    }
}
