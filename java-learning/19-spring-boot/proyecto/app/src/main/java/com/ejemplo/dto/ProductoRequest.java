package com.ejemplo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequest(
    @NotBlank(message = "Nombre es requerido")
    @Size(max = 150)
    String nombre,

    String descripcion,

    @NotNull(message = "Precio es requerido")
    @DecimalMin(value = "0.01", message = "Precio debe ser mayor a 0")
    BigDecimal precio,

    @Min(value = 0, message = "Stock no puede ser negativo")
    int stock,

    String categoria
) {}
