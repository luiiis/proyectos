package com.academia.ws.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

/**
 * Controller que maneja mensajes WebSocket Y endpoints REST para disparar notificaciones.
 */
@RestController
public class NotificacionController {

    private final SimpMessagingTemplate messaging;

    public NotificacionController(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    /**
     * Endpoint REST: simula registrar una venta y notifica a TODOS los conectados.
     * POST /api/ventas/registrar {"producto": "Laptop", "total": 18999}
     */
    @PostMapping("/api/ventas/registrar")
    public Map<String, Object> registrarVenta(@RequestBody Map<String, Object> venta) {
        // Agregar timestamp
        venta.put("timestamp", Instant.now().toString());
        venta.put("id", System.currentTimeMillis());

        // Enviar a TODOS los clientes suscritos a /topic/ventas
        messaging.convertAndSend("/topic/ventas", venta);

        // Enviar alerta si el monto es alto
        double total = ((Number) venta.get("total")).doubleValue();
        if (total > 10000) {
            messaging.convertAndSend("/topic/alertas",
                    Map.of("tipo", "VENTA_GRANDE", "mensaje", "Venta de $" + total + " registrada!", "timestamp", Instant.now().toString()));
        }

        return Map.of("status", "OK", "mensaje", "Venta registrada y notificada en tiempo real");
    }

    /**
     * Endpoint REST: enviar notificacion custom
     * POST /api/notificar {"mensaje": "Stock bajo en Laptop HP"}
     */
    @PostMapping("/api/notificar")
    public Map<String, String> notificar(@RequestBody Map<String, String> body) {
        messaging.convertAndSend("/topic/alertas",
                Map.of("tipo", "ALERTA", "mensaje", body.get("mensaje"), "timestamp", Instant.now().toString()));
        return Map.of("status", "Notificacion enviada");
    }

    /**
     * WebSocket handler: cuando un cliente ENVIA un mensaje via WebSocket
     * El cliente envia a: /app/chat
     * Se reenvía a: /topic/chat (todos los suscritos lo reciben)
     */
    @MessageMapping("/chat")  // Cliente envia a /app/chat
    @SendTo("/topic/chat")    // Se reenvía a /topic/chat
    public Map<String, String> chat(Map<String, String> mensaje) {
        return Map.of(
                "usuario", mensaje.getOrDefault("usuario", "Anonimo"),
                "texto", mensaje.getOrDefault("texto", ""),
                "timestamp", Instant.now().toString()
        );
    }
}
