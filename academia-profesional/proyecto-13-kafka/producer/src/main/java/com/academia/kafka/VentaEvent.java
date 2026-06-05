package com.academia.kafka;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record VentaEvent(
    Long ventaId,
    String clienteEmail,
    BigDecimal total,
    List<String> productos,
    Instant timestamp
) {
    public VentaEvent(Long ventaId, String clienteEmail, BigDecimal total, List<String> productos) {
        this(ventaId, clienteEmail, total, productos, Instant.now());
    }
}
