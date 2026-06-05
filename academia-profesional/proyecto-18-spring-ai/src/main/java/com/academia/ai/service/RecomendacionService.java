package com.academia.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio de recomendaciones inteligentes.
 * Combina datos de la BD + IA para sugerir productos.
 */
@Service
public class RecomendacionService {

    private final ChatClient chatClient;
    private final JdbcTemplate jdbc;

    public RecomendacionService(ChatClient.Builder builder, JdbcTemplate jdbc) {
        this.chatClient = builder.build();
        this.jdbc = jdbc;
    }

    public Map<String, Object> recomendar(String necesidad, double presupuesto) {
        // Buscar productos dentro del presupuesto
        List<Map<String, Object>> opciones = jdbc.queryForList(
                "SELECT nombre, precio, categoria, caracteristicas FROM productos " +
                "WHERE precio <= ? AND activo = true ORDER BY precio DESC",
                presupuesto
        );

        if (opciones.isEmpty()) {
            return Map.of("respuesta", "No tenemos productos dentro de ese presupuesto.",
                    "opciones", 0);
        }

        // Construir prompt para IA
        StringBuilder catalogo = new StringBuilder();
        for (var p : opciones) {
            catalogo.append(String.format("- %s ($%s): %s\n",
                    p.get("nombre"), p.get("precio"), p.get("caracteristicas")));
        }

        String prompt = String.format("""
                El cliente necesita: %s
                Presupuesto maximo: $%,.2f
                
                Productos disponibles:
                %s
                
                Recomienda el MEJOR producto para su necesidad.
                Explica POR QUE es la mejor opcion.
                Si hay alternativas, mencionalas brevemente.
                Responde en espanol, de forma amable y profesional.
                """, necesidad, presupuesto, catalogo);

        String respuesta = chatClient.prompt()
                .system("Eres un asesor de tecnologia experto. Haces recomendaciones honestas y utiles.")
                .user(prompt)
                .call()
                .content();

        return Map.of(
                "necesidad", necesidad,
                "presupuesto", presupuesto,
                "respuesta", respuesta,
                "opcionesEvaluadas", opciones.size()
        );
    }
}
