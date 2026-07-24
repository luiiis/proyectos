package com.softwarelee.tareas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * MANEJO GLOBAL DE ERRORES.
 *
 * @RestControllerAdvice intercepta TODAS las excepciones de TODOS los controllers.
 * En vez de que el usuario vea un error feo (stack trace), ve un JSON limpio.
 *
 * Sin esto: el usuario ve "500 Internal Server Error" con HTML feo.
 * Con esto: el usuario ve {"success": false, "message": "Tarea no encontrada con ID: 5"}
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Tarea no encontrada → 404
     */
    @ExceptionHandler(TareaNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(TareaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "success", false,
                "message", ex.getMessage(),
                "status", 404,
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * Validaciones fallidas (@Valid) → 400
     * Ejemplo: título vacío, descripción muy larga
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "message", "Error de validación",
                "errors", errores,
                "status", 400,
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * Cualquier otro error no manejado → 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Error interno del servidor",
                "detail", ex.getMessage(),
                "status", 500,
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
