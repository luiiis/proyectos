package com.empresa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Endpoint de salud para verificar que la API está corriendo.
 * Útil para Docker healthchecks y monitoreo.
 */
@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "app", "Proyecto Final - Sistema Empresarial",
            "status", "UP",
            "version", "1.0.0",
            "timestamp", Instant.now().toString(),
            "endpoints", Map.of(
                "productos", "/api/productos",
                "reportes", "/api/reportes/ventas-mensuales",
                "swagger", "/swagger-ui.html",
                "health", "/actuator/health"
            )
        );
    }
}
