package com.fullstack.auth.service;

import com.fullstack.auth.dto.AuthRequest;
import com.fullstack.auth.dto.AuthResponse;
import com.fullstack.auth.dto.RegisterRequest;
import com.fullstack.auth.entity.mysql.Role;
import com.fullstack.auth.entity.mysql.User;
import com.fullstack.auth.repository.mysql.RoleRepository;
import com.fullstack.auth.repository.mysql.UserRepository;
import com.fullstack.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AuthService.
 * 
 * ¿Qué es un test unitario?
 * - Prueba UNA sola clase/método de forma aislada.
 * - Las dependencias se "mockean" (simulan) con Mockito.
 * - No necesita base de datos, servidor web, ni nada externo.
 * - Son rápidos (milisegundos) y se ejecutan en cualquier entorno.
 * 
 * Estructura AAA (Arrange-Act-Assert):
 * - Arrange: Preparar los datos y mocks necesarios.
 * - Act: Ejecutar el método que queremos probar.
 * - Assert: Verificar que el resultado es el esperado.
 * 
 * Anotaciones clave:
 * - @Mock: Crea un objeto simulado (no real) de la dependencia.
 * - @InjectMocks: Crea la instancia real del servicio e inyecta los mocks.
 * - @Test: Marca un método como caso de prueba.
 * - @DisplayName: Nombre legible del test (aparece en reportes).
 */
@ExtendWith(MockitoExtension.class)  // Activa Mockito para esta clase
class AuthServiceTest {

    // Mocks: objetos simulados que reemplazan las dependencias reales
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    // La clase que estamos probando (con los mocks inyectados)
    @InjectMocks
    private AuthService authService;

    // Datos de prueba reutilizables
    private RegisterRequest registerRequest;
    private AuthRequest loginRequest;
    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Preparar datos que se usan en múltiples tests
        userRole = Role.builder().id(1L).name("USER").description("Usuario estándar").build();

        registerRequest = RegisterRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .build();

        loginRequest = AuthRequest.builder()
                .username("testuser")
                .password("password123")
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("$2a$12$encodedPassword")
                .firstName("Test")
                .lastName("User")
                .roles(roles)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Registro exitoso - debe crear usuario y devolver tokens")
    void register_Success() {
        // ARRANGE: Configurar qué devuelven los mocks
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$12$encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("access-token-123");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refresh-token-456");

        // ACT: Ejecutar el método que probamos
        AuthResponse response = authService.register(registerRequest);

        // ASSERT: Verificar resultados
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-123");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token-456");
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getRoles()).contains("USER");

        // Verificar que se llamaron los métodos esperados
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password123");
    }

    @Test
    @DisplayName("Registro fallido - username ya existe")
    void register_UsernameExists_ThrowsException() {
        // ARRANGE: Simular que el username ya existe
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // ACT & ASSERT: Verificar que lanza excepción
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El username ya existe");

        // Verificar que NUNCA se intentó guardar el usuario
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registro fallido - email ya registrado")
    void register_EmailExists_ThrowsException() {
        // ARRANGE
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El email ya está registrado");
    }

    @Test
    @DisplayName("Login exitoso - debe autenticar y devolver tokens")
    void login_Success() {
        // ARRANGE
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("access-token-789");
        when(jwtService.generateRefreshToken(testUser)).thenReturn("refresh-token-012");

        // ACT
        AuthResponse response = authService.login(loginRequest);

        // ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-789");
        assertThat(response.getUsername()).isEqualTo("testuser");

        // Verificar que se llamó al AuthenticationManager
        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Login fallido - usuario no encontrado")
    void login_UserNotFound_ThrowsException() {
        // ARRANGE: AuthenticationManager pasa, pero el usuario no se encuentra
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");
    }
}
