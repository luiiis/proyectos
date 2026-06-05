package com.empresa.dto;

import java.math.BigDecimal;

/**
 * Record: DTO inmutable para transferir datos entre capas.
 * Java genera automáticamente: constructor, getters, equals, hashCode, toString.
 */
public record ProductoDto(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String sku,
    Integer categoriaId,
    Integer proveedorId
) {}
