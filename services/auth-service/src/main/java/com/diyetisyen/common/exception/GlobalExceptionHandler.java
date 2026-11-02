package com.diyetisyen.common.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Component("commonGlobalExceptionHandler")
public class GlobalExceptionHandler {

    // DTO içi @Valid hatası
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                field -> field.getField(),
                                field -> field.getDefaultMessage(),
                                (a, b) -> a
                        )
                );

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "message", "Geçersiz istek verisi.",
                        "errors", errors
                )
        );
    }

    // Parametre doğrulama hatası
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations()
                .stream()
                .collect(
                        java.util.stream.Collectors.toMap(
                                v -> v.getPropertyPath().toString(),
                                v -> v.getMessage(),
                                (a, b) -> a
                        )
                );

        return ResponseEntity.badRequest().body(
                Map.of(
                        "status", 400,
                        "message", "Geçersiz parametre.",
                        "errors", errors
                )
        );
    }

    // Diğer tüm hatalar
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body(
                Map.of(
                        "status", 500,
                        "message", "Sunucuda beklenmeyen bir hata oluştu.",
                        "error", ex.getMessage()
                )
        );
    }
}
