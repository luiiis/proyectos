package com.academia.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Proyecto 13: Kafka Consumer
 * Escucha eventos del topic "ventas.creada" y los procesa.
 * En producción: enviaría email, SMS, push notification, etc.
 */
@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) { SpringApplication.run(ConsumerApplication.class, args); }
}

@Service
class NotificacionConsumer {

    /**
     * Escucha el topic "ventas.creada"
     * Cada vez que el producer publica un mensaje, este método se ejecuta automáticamente.
     */
    @KafkaListener(topics = "ventas.creada", groupId = "notificaciones-group")
    public void procesarVenta(String mensaje) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("📧 [" + LocalDateTime.now() + "] NUEVO EVENTO RECIBIDO");
        System.out.println("Topic: ventas.creada");
        System.out.println("Payload: " + mensaje);
        System.out.println("Acción: Enviando notificación al cliente...");
        System.out.println("✓ Notificación procesada exitosamente");
        System.out.println("═══════════════════════════════════════════════════");
    }

    /**
     * Escucha el topic "stock.actualizado"
     * Cuando se vende un producto, se notifica al equipo de inventario.
     */
    @KafkaListener(topics = "stock.actualizado", groupId = "inventario-group")
    public void procesarStock(String mensaje) {
        System.out.println("📦 [" + LocalDateTime.now() + "] STOCK EVENT");
        System.out.println("Payload: " + mensaje);
        System.out.println("Acción: Verificando stock mínimo...");
        System.out.println("───────────────────────────────────────────────────");
    }
}
