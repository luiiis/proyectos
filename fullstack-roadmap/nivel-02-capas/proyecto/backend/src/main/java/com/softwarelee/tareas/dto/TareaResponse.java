package com.softwarelee.tareas.dto;

import java.time.LocalDateTime;

/**
 * DTO de salida (lo que el servidor DEVUELVE al cliente).
 *
 * Contiene TODO lo que el cliente necesita ver, incluyendo datos generados por el sistema.
 */
public record TareaResponse(
    Long id,
    String titulo,
    String descripcion,
    boolean completada,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}
