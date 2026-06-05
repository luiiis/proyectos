# Módulo 22: Spring Security

## Configuración básica
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```

## Control de acceso por método
```java
@PreAuthorize("hasRole('ADMIN')")
public void eliminarUsuario(Long id) { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
public void crearProducto(ProductoDto dto) { ... }

@PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
public UsuarioDto verPerfil(Long id) { ... }
```

## Ejercicios
1. Configura seguridad con 3 roles: ADMIN, GERENTE, VENDEDOR
2. Protege endpoints según la matriz de permisos del negocio
3. Implementa UserDetailsService con usuarios de la BD
4. Agrega rate limiting por IP
