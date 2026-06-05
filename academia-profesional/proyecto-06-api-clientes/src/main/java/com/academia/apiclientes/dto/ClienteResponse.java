package com.academia.apiclientes.dto;

import java.time.Instant;

public record ClienteResponse(
    Long id,
    String nombre,
    String apellido,
    String email,
    String telefono,
    String ciudad,
    Instant createdAt
) {}
