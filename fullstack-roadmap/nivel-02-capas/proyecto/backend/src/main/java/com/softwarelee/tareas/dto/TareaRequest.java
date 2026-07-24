package com.softwarelee.tareas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada (lo que el cliente ENVÍA al crear/modificar una tarea).
 *
 * ¿Por qué un DTO y no usar directamente el Model?
 * 1. El cliente NO debe enviar el ID (lo genera el sistema)
 * 2. El cliente NO debe enviar fechas (las genera el sistema)
 * 3. El cliente NO debe enviar "completada" al crear (siempre empieza en false)
 * 4. Puedes validar SOLO lo que envía el cliente
 *
 * Record = clase inmutable que autogenera constructor, getters, equals, hashCode, toString.
 */
public record TareaRequest(

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    String titulo,

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    String descripcion

) {}
