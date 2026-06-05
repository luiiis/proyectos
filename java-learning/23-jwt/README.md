# Módulo 23: JWT (JSON Web Tokens)

## Flujo de autenticación
```
1. POST /api/auth/login {username, password}
2. Backend valida credenciales → genera JWT
3. Responde: {accessToken, refreshToken}
4. Frontend guarda tokens
5. Cada request incluye: Authorization: Bearer <token>
6. Backend valida token en cada request (JwtFilter)
```

## Implementación
```java
@Service
public class JwtService {
    @Value("${app.jwt.secret}") private String secret;
    @Value("${app.jwt.expiration}") private long expiration;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getKey())
            .compact();
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isExpired(token);
    }
}
```

## Refresh Token
```java
@PostMapping("/auth/refresh")
public AuthResponse refresh(@RequestBody RefreshRequest request) {
    String email = jwtService.extractUsername(request.refreshToken());
    UserDetails user = userService.loadUserByUsername(email);
    if (jwtService.isValid(request.refreshToken(), user)) {
        String newAccess = jwtService.generateToken(user);
        return new AuthResponse(newAccess, request.refreshToken());
    }
    throw new UnauthorizedException("Refresh token inválido");
}
```

## Ejercicios
1. Implementa login + registro con JWT
2. Agrega refresh token con expiración de 7 días
3. Implementa logout (blacklist de tokens en Redis)
4. Agrega claims personalizados (roles, sucursal_id)
