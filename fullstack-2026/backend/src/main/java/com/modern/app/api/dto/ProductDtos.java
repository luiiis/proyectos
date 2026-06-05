package com.modern.app.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTOs de Producto usando Records (Java 16+).
 *
 * ¿Por qué Records en lugar de clases con Lombok?
 * 1. INMUTABLES: no se pueden modificar después de crear (thread-safe)
 * 2. COMPACTOS: una línea vs 30+ líneas con Lombok
 * 3. SEMÁNTICOS: un Record ES un contenedor de datos (su propósito es claro)
 * 4. NATIVOS: no necesitan librería externa (Lombok puede fallar con GraalVM)
 * 5. PATTERN MATCHING: funcionan con switch expressions y deconstrucción
 *
 * Se agrupan en un archivo porque son pequeños y relacionados.
 * En proyectos grandes, cada uno iría en su propio archivo.
 */
public sealed interface ProductDtos {

    // ═══════ Request DTOs (lo que envía el cliente) ═══════

    record CreateProduct(
            @NotBlank(message = "Nombre es requerido")
            @Size(max = 150)
            String name,

            @Size(max = 1000)
            String description,

            @NotNull(message = "Precio es requerido")
            @DecimalMin(value = "0.01", message = "Precio debe ser mayor a 0")
            BigDecimal price,

            @Min(value = 0, message = "Stock no puede ser negativo")
            int stock,

            @Size(max = 50)
            String category,

            @Size(max = 50)
            String sku,

            String imageUrl
    ) implements ProductDtos {}

    record UpdateProduct(
            String name,
            String description,
            BigDecimal price,
            String category,
            String sku,
            String imageUrl,
            Boolean active
    ) implements ProductDtos {}

    record StockMovement(
            @Min(value = 1, message = "Cantidad debe ser al menos 1")
            int quantity,
            String reason
    ) implements ProductDtos {}

    // ═══════ Response DTOs (lo que devuelve el servidor) ═══════

    record ProductResponse(
            Long id,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String category,
            String sku,
            String imageUrl,
            boolean active,
            Instant createdAt
    ) implements ProductDtos {}

    // ═══════ Respuesta genérica de API ═══════
    record ApiResult<T>(
            boolean success,
            String message,
            T data,
            Instant timestamp
    ) {
        public ApiResult(boolean success, String message, T data) {
            this(success, message, data, Instant.now());
        }

        public static <T> ApiResult<T> ok(T data) {
            return new ApiResult<>(true, "OK", data);
        }

        public static <T> ApiResult<T> ok(String message, T data) {
            return new ApiResult<>(true, message, data);
        }

        public static <T> ApiResult<T> error(String message) {
            return new ApiResult<>(false, message, null);
        }
    }
}
