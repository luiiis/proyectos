package com.academia.productos.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(
    @NotBlank String nombre,
    String descripcion,
    @NotNull @DecimalMin("0.01") BigDecimal precio,
    BigDecimal costo,
    @Min(0) int stock,
    String sku,
    String categoria
) {}
