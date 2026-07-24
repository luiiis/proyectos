package com.softwarelee.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * FILTRO JWT: intercepta CADA request HTTP.
 *
 * Flujo:
 * 1. Busca el header "Authorization: Bearer <token>"
 * 2. Si existe, extrae y valida el token
 * 3. Si es válido, crea el Authentication y lo pone en el SecurityContext
 * 4. Spring Security ya sabe quién es el usuario
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer ", pasar al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitar "Bearer ")
        String token = authHeader.substring(7);

        // 4. Validar el token
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.extractUsername(token);
            List<String> roles = jwtService.extractRoles(token);

            // 5. Crear las autoridades (roles) para Spring Security
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            // 6. Crear el objeto de autenticación
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // 7. Guardarlo en el SecurityContext (ahora Spring sabe quién es)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 8. Continuar la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
