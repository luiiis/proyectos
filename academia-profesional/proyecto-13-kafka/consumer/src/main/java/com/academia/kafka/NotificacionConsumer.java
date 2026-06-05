package com.academia.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificacionConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificacionConsumer.class);

    @KafkaListener(topics = "venta-creada", groupId = "notificaciones-group")
    public void onVentaCreada(Map<String, Object> evento) {
        log.info("═══════════════════════════════════════════════");
        log.info("📧 EVENTO RECIBIDO: venta-creada");
        log.info("   Venta ID: {}", evento.get("ventaId"));
        log.info("   Cliente: {}", evento.get("clienteEmail"));
        log.info("   Total: ${}", evento.get("total"));
        log.info("   Productos: {}", evento.get("productos"));
        log.info("   Timestamp: {}", evento.get("timestamp"));
        log.info("═══════════════════════════════════════════════");

        // Aquí iría la lógica real:
        // - Enviar email de confirmación al cliente
        // - Enviar SMS
        // - Actualizar dashboard en tiempo real
        // - Registrar en BD de notificaciones

        log.info("✓ Notificación procesada para: {}", evento.get("clienteEmail"));
    }
}
