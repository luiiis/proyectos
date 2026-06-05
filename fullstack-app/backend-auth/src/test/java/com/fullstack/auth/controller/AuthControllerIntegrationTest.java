package com.fullstack.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.auth.dto.AuthRequest;
import com.fullstack.auth.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de INTEGRACIÓN para AuthController.
 * 
 * ¿Diferencia con tests unitarios?
 * - Tests unitarios: prueban UNA clase aislada con mocks.
 * - Tests de integración: levantan el contexto completo de Spring Boot.
 *   Prueban que todos los componentes funcionan JUNTOS (controller + service + repository + BD).
 * 
 * @SpringBootTest: Levanta toda la aplicación Spring Boot.
 * @AutoConfigureMockMvc: Configura MockMvc para simular peticiones HTTP sin servidor real.
 * @ActiveProfiles("test"): Usa application-test.yml (H2 en memoria).
 * @TestMethodOrder: Los tests se ejecutan en orden (importante: primero registrar, luego login).
 * 
 * MockMvc simula peticiones HTTP:
 * - mockMvc.perform(post("/api/auth/login")...) → simula un POST real
 * - .andExpect(status().isOk()) → verifica que respondió 200
 * - .andExpect(jsonPath("$.data.accessToken").exists()) → verifica el JSON de respuesta
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;  // Simula peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper;  // Convierte objetos Java ↔ JSON

    @Test
    @Order(1)
    @DisplayName("POST /api/auth/register - Registro exitoso")
    void register_ValidData_Returns200() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("integrationuser")
                .email("integration@test.com")
                .password("password123")
                .firstName("Integration")
                .lastName("Test")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(jsonPath("$.data.username").value("integrationuser"));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/auth/register - Username duplicado retorna error")
    void register_DuplicateUsername_Returns400() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .username("integrationuser")  // Ya existe del test anterior
                .email("otro@test.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El username ya existe"));
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/auth/login - Login exitoso")
    void login_ValidCredentials_Returns200() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .username("integrationuser")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.roles").isArray());
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/auth/login - Credenciales inválidas retorna 401")
    void login_InvalidCredentials_Returns401() throws Exception {
        AuthRequest request = AuthRequest.builder()
                .username("integrationuser")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    @DisplayName("POST /api/auth/register - Validación de campos requeridos")
    void register_MissingFields_Returns400() throws Exception {
        // Enviar request vacío
        RegisterRequest request = RegisterRequest.builder().build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
