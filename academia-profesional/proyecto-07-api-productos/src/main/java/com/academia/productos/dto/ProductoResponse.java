package com.academia.productos.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductoResponse(
    Long id, String nombre, String descripcion,
    BigDecimal precio, BigDecimal costo, int stock,
    String sku, String categoria, Instant createdAt
) {}
