package com.invex.employee.mx.exceptions;

import com.invex.employee.mx.models.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String errorCode, HttpServletRequest request, Map<String, List<String>> details) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request != null ? request.getRequestURI() : null,
                errorCode,
                details
        );
        return new ResponseEntity<>(body, status);
    }

	@ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormat(NumberFormatException ex, HttpServletRequest request) {
        log.warn("Number format error: {}", ex.getMessage());
        Map<String, List<String>> details = new HashMap<>();
        details.put("number", Collections.singletonList("Invalid numeric value"));
        return buildResponse(HttpStatus.BAD_REQUEST, "Valor numérico inválido", "INVALID_NUMBER_FORMAT", request, details);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        log.warn("API exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        // Map error codes to HTTP status if you want more granularity; by default 400 for client errors
        HttpStatus status = switch (ex.getErrorCode()) {
            case "INVALID_DATE_FORMAT" -> HttpStatus.BAD_REQUEST;
            case "RESOURCE_NOT_FOUND" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };
        return buildResponse(status, ex.getMessage(), ex.getErrorCode(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        log.info("Validation failed: {}", errors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de validación de entrada", "VALIDATION_ERROR", request, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("JSON parse error: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "JSON inválido o datos mal formateados", "MALFORMED_JSON", request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDB(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Database error", ex);
        return buildResponse(HttpStatus.CONFLICT, "Error de integridad en la base de datos", "DB_CONSTRAINT_VIOLATION", request, null);
    }
}