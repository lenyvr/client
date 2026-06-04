package com.devsu.fintech.infrastructure.adapter.in.rest;

import com.devsu.fintech.domain.exception.ClientAlreadyExistsException;
import com.devsu.fintech.domain.exception.ClientNotFoundException;
import com.devsu.fintech.domain.exception.InvalidClientDataException;
import com.devsu.fintech.domain.exception.InvalidPersonDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Centralized error handling: translates domain and validation errors into HTTP responses
 * using RFC 7807 {@link ProblemDetail}.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ProblemDetail handleAlreadyExists(ClientAlreadyExistsException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ProblemDetail handleNotFound(ClientNotFoundException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({InvalidPersonDataException.class, InvalidClientDataException.class})
    public ProblemDetail handleInvalidDomainData(RuntimeException ex) {
        log.warn("Invalid data: {}", ex.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", details);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, details);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "The operation violates a uniqueness or referential constraint (e.g. duplicated client code or identification_type_id/gender_id does not exists).");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
    }
}
