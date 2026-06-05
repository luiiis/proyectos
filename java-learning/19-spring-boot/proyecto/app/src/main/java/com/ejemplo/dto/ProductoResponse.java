package com.ejemplo.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductoResponse(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    int stock,
    String categoria,
    Instant createdAt
) {}
