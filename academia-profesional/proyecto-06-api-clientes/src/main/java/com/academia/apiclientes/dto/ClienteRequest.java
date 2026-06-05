package com.academia.apiclientes.dto;

import jakarta.validation.constraints.*;

public record ClienteRequest(
    @NotBlank(message = "Nombre es requerido")
    @Size(min = 2, max = 50)
    String nombre,

    @NotBlank(message = "Apellido es requerido")
    String apellido,

    @Email(message = "Email debe ser válido")
    String email,

    @Pattern(regexp = "^\\d{2}-\\d{4}-\\d{4}$", message = "Formato: XX-XXXX-XXXX")
    String telefono,

    String ciudad
) {}
