package com.academia.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio de Chat con IA.
 * 
 * Dos modos:
 * 1. Chat simple: pregunta directa a GPT (sin contexto de tu BD)
 * 2. RAG: busca productos relevantes en BD → los pasa como contexto a GPT
 */
@Service
public class ChatService {

    private final ChatClient chatClient;
    private final JdbcTemplate jdbc;

    public ChatService(ChatClient.Builder chatClientBuilder, JdbcTemplate jdbc) {
        this.chatClient = chatClientBuilder.build();
        this.jdbc = jdbc;
    }

    /**
     * Chat simple: GPT responde con su conocimiento general
     */
    public String responder(String mensaje) {
        return chatClient.prompt()
                .system("Eres un asistente de ventas de una tienda de tecnologia. " +
                        "Responde de forma amable y concisa en espanol.")
                .user(mensaje)
                .call()
                .content();
    }

    /**
     * RAG (Retrieval Augmented Generation):
     * 1. Busca productos relevantes en la BD
     * 2. Los pasa como contexto a GPT
     * 3. GPT responde basandose en TUS datos reales
     */
    public Map<String, Object> consultarConRAG(String pregunta) {
        // Paso 1: Buscar productos relevantes (busqueda simple por ahora)
        List<Map<String, Object>> productos = jdbc.queryForList(
                "SELECT nombre, precio, categoria, caracteristicas FROM productos " +
                "WHERE activo = true ORDER BY nombre LIMIT 10"
        );

        // Paso 2: Construir contexto con los productos encontrados
        StringBuilder contexto = new StringBuilder("Productos disponibles en nuestra tienda:\n");
        for (var p : productos) {
            contexto.append(String.format("- %s ($%s) [%s]: %s\n",
                    p.get("nombre"), p.get("precio"), p.get("categoria"), p.get("caracteristicas")));
        }

        // Paso 3: Enviar a GPT con contexto
        String respuesta = chatClient.prompt()
                .system("Eres un asesor de ventas experto. Usa SOLO los productos del contexto para responder. " +
                        "Si no hay un producto que cumpla, dilo honestamente. Responde en espanol.")
                .user(contexto + "\nPregunta del cliente: " + pregunta)
                .call()
                .content();

        return Map.of(
                "pregunta", pregunta,
                "respuesta", respuesta,
                "productosConsultados", productos.size()
        );
    }
}
