package com.fullstack.auth.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuración de Rate Limiting usando Bucket4j.
 * 
 * ¿Qué es Rate Limiting?
 * - Es un mecanismo para limitar cuántas peticiones puede hacer un cliente en un período de tiempo.
 * - Protege contra ataques de fuerza bruta, DDoS y abuso de la API.
 * 
 * ¿Cómo funciona Bucket4j?
 * - Usa el algoritmo "Token Bucket" (cubeta de tokens).
 * - Cada cliente tiene una "cubeta" con N tokens.
 * - Cada petición consume 1 token.
 * - Los tokens se regeneran automáticamente con el tiempo.
 * - Si no hay tokens disponibles → se rechaza la petición (HTTP 429).
 * 
 * Ejemplo visual:
 * - Cubeta con 20 tokens, se regeneran 20 cada minuto.
 * - Si un usuario hace 20 peticiones en 10 segundos, se queda sin tokens.
 * - Debe esperar a que se regeneren para seguir haciendo peticiones.
 * 
 * En este proyecto:
 * - Login: 5 intentos por minuto (protección contra fuerza bruta)
 * - API general: 100 peticiones por minuto por IP
 */
@Configuration
public class RateLimitConfig {

    // Mapa que almacena un bucket por cada IP/usuario
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Obtiene o crea un bucket para peticiones generales de API.
     * Límite: 100 peticiones por minuto por IP.
     */
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::createStandardBucket);
    }

    /**
     * Obtiene o crea un bucket específico para login.
     * Límite: 5 intentos por minuto por IP (más restrictivo).
     */
    public Bucket resolveLoginBucket(String key) {
        return buckets.computeIfAbsent("login:" + key, this::createLoginBucket);
    }

    /**
     * Bucket estándar: 100 tokens, se rellenan 100 cada minuto.
     * Esto significa: máximo 100 peticiones por minuto.
     */
    private Bucket createStandardBucket(String key) {
        Bandwidth limit = Bandwidth.classic(
                100,                              // Capacidad máxima: 100 tokens
                Refill.greedy(100, Duration.ofMinutes(1))  // Se regeneran 100 tokens cada minuto
        );
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Bucket de login: 5 tokens, se rellenan 5 cada minuto.
     * Más restrictivo para prevenir ataques de fuerza bruta.
     */
    private Bucket createLoginBucket(String key) {
        Bandwidth limit = Bandwidth.classic(
                5,                                // Capacidad máxima: 5 tokens
                Refill.greedy(5, Duration.ofMinutes(1))    // Se regeneran 5 tokens cada minuto
        );
        return Bucket.builder().addLimit(limit).build();
    }
}
