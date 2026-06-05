package com.fullstack.auth.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro HTTP que aplica Rate Limiting a cada petición.
 * 
 * ¿Cómo funciona?
 * 1. Se ejecuta ANTES de cualquier otro procesamiento (Order 1).
 * 2. Obtiene la IP del cliente.
 * 3. Si es un endpoint de login → usa el bucket restrictivo (5/min).
 * 4. Si es cualquier otro endpoint → usa el bucket estándar (100/min).
 * 5. Intenta consumir 1 token del bucket.
 * 6. Si hay tokens disponibles → deja pasar la petición.
 * 7. Si NO hay tokens → responde con HTTP 429 (Too Many Requests).
 * 
 * Headers de respuesta:
 * - X-Rate-Limit-Remaining: cuántos tokens quedan.
 * - X-Rate-Limit-Retry-After-Seconds: cuánto esperar si se excedió el límite.
 */
@Component
@Order(1)  // Se ejecuta primero, antes del filtro JWT
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitConfig rateLimitConfig;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtener la IP del cliente (considera proxies)
        String clientIp = getClientIp(request);
        String requestUri = request.getRequestURI();

        // Seleccionar el bucket apropiado según el endpoint
        Bucket bucket;
        if (requestUri.contains("/api/auth/login")) {
            // Login: más restrictivo (5 intentos/min)
            bucket = rateLimitConfig.resolveLoginBucket(clientIp);
        } else {
            // General: 100 peticiones/min
            bucket = rateLimitConfig.resolveBucket(clientIp);
        }

        // Intentar consumir 1 token
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // ✓ Hay tokens disponibles → dejar pasar
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            // ✗ Sin tokens → rechazar con 429
            long waitForRefillNanos = probe.getNanosToWaitForRefill();
            long waitSeconds = waitForRefillNanos / 1_000_000_000;

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitSeconds));
            response.getWriter().write("""
                    {
                        "success": false,
                        "message": "Demasiadas peticiones. Intenta de nuevo en %d segundos.",
                        "retryAfterSeconds": %d
                    }
                    """.formatted(waitSeconds, waitSeconds));
        }
    }

    /**
     * Obtiene la IP real del cliente, considerando que puede estar detrás de un proxy/load balancer.
     * El header X-Forwarded-For contiene la IP original cuando hay un proxy de por medio.
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For puede tener múltiples IPs: "clientIP, proxy1, proxy2"
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
