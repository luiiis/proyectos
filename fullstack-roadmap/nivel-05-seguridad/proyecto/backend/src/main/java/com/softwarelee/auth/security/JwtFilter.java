package com.softwarelee.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * FILTRO JWT: Intercepta CADA request HTTP y valida el token.
 *
 * Flujo:
 * 1. Llega un request
 * 2. Este filtro revisa el header "Authorization: Bearer xxxxx"
 * 3. Si hay token → lo valida → carga el usuario → lo pone en SecurityContext
 * 4. Si no hay token o es inválido → deja pasar (Spring Security decidirá si el endpoint es público)
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Extraer el header Authorization
        String header = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer " → pasar sin autenticar
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitar "Bearer ")
        String token = header.substring(7);

        try {
            // 4. Validar token y extraer username
            if (jwtService.isValid(token)) {
                String username = jwtService.extractUsername(token);

                // 5. Cargar los datos del usuario desde la BD
                var userDetails = userDetailsService.loadUserByUsername(username);

                // 6. Crear objeto de autenticación y ponerlo en el contexto
                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ignored) {
            // Token inválido → no autenticar (el endpoint decidirá si requiere auth)
        }

        // 7. Continuar con el siguiente filtro
        chain.doFilter(request, response);
    }
}
