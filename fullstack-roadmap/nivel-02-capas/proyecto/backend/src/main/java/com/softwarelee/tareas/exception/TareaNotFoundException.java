package com.softwarelee.tareas.exception;

/**
 * Excepción personalizada: se lanza cuando una tarea no existe.
 * Extiende RuntimeException (unchecked) → no obliga a try/catch.
 */
public class TareaNotFoundException extends RuntimeException {
    public TareaNotFoundException(String message) {
        super(message);
    }
}
