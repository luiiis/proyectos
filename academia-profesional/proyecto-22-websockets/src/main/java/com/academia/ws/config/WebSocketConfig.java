package com.academia.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuracion de WebSocket con STOMP.
 * STOMP = Simple Text Oriented Messaging Protocol (protocolo sobre WebSocket)
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic = mensajes broadcast (todos los suscritos reciben)
        // /queue = mensajes punto a punto (solo 1 receptor)
        config.enableSimpleBroker("/topic", "/queue");
        
        // Prefijo para mensajes que ENVIAN los clientes al servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint donde los clientes se CONECTAN
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // CORS
                .withSockJS();  // Fallback para navegadores sin WebSocket nativo
    }
}
