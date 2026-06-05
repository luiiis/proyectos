# Módulo 6: Tests - Qué Detecta Cada Test

## 6.1 Concepto: ¿Por Qué Testear?

Sin tests: cambias una línea → rompes algo en otro lado → no te enteras hasta producción.
Con tests: cambias una línea → los tests fallan → te enteras en 5 segundos.

## 6.2 AuthServiceTest - Qué Detecta Cada Test

### Test: `register_Success`
```
¿Qué prueba?
  Que el registro funciona correctamente cuando los datos son válidos.

¿Qué errores detecta?
  - Si alguien cambia el orden de operaciones en register()
  - Si se olvida de encriptar el password
  - Si no se asigna el rol USER por defecto
  - Si no se genera el token JWT después del registro
  - Si no se guarda el usuario en la BD

¿Cuándo fallaría?
  - Si cambias passwordEncoder.encode() por otra cosa
  - Si eliminas la línea userRepository.save()
  - Si jwtService.generateToken() lanza excepción
```

### Test: `register_UsernameExists_ThrowsException`
```
¿Qué prueba?
  Que NO se puede registrar un usuario con username duplicado.

¿Qué errores detecta?
  - Si alguien quita la validación de username duplicado
  - Si la excepción tiene un mensaje diferente al esperado
  - Si se intenta guardar el usuario a pesar del duplicado

¿Cuándo fallaría?
  - Si eliminas el if (userRepository.existsByUsername(...))
  - Si cambias el mensaje de error
```

### Test: `login_Success`
```
¿Qué prueba?
  Que el login devuelve tokens cuando las credenciales son correctas.

¿Qué errores detecta?
  - Si el AuthenticationManager no se invoca (no valida credenciales)
  - Si no se genera el access token
  - Si el username en la respuesta no coincide

¿Cuándo fallaría?
  - Si cambias la lógica de autenticación
  - Si el JwtService falla al generar tokens
```

### Test: `login_UserNotFound_ThrowsException`
```
¿Qué prueba?
  Que si el usuario no existe en la BD, se lanza excepción.

¿Qué errores detecta?
  - Si alguien quita el .orElseThrow()
  - Si devuelve null en lugar de lanzar excepción
  - Si el mensaje de error cambia

¿Cuándo fallaría?
  - Si cambias Optional.orElseThrow() por Optional.orElse(null)
```

## 6.3 ProductServiceTest - Qué Detecta Cada Test

### Test: `getAllProducts_ReturnsActiveProducts`
```
¿Qué prueba?
  Que solo devuelve productos ACTIVOS (no los eliminados con soft delete).

¿Qué errores detecta?
  - Si alguien cambia findByIsActiveTrue() por findAll()
  - Si el mapeo a DTO pierde campos
  - Si devuelve lista vacía cuando hay productos
```

### Test: `getProductById_NotExists_ThrowsException`
```
¿Qué prueba?
  Que buscar un producto inexistente lanza excepción (no devuelve null).

¿Qué errores detecta?
  - NullPointerException si se quita el orElseThrow()
  - Respuesta vacía sin error al frontend
```

### Test: `deleteProduct_SetsInactive`
```
¿Qué prueba?
  Que "eliminar" un producto solo lo marca como inactivo (soft delete).

¿Qué errores detecta?
  - Si alguien cambia soft delete por hard delete (productRepository.delete())
  - Si no se actualiza el campo isActive
  - Pérdida de datos históricos
```

### Test: `removeStock_InsufficientStock_ThrowsException`
```
¿Qué prueba?
  Que NO se puede retirar más stock del disponible.

¿Qué errores detecta?
  - Stock negativo (vender más de lo que hay)
  - Si se quita la validación de stock suficiente
  - Inconsistencia en inventario
```

## 6.4 AuthControllerIntegrationTest - Qué Detecta

### Diferencia con tests unitarios:
```
Unitario: prueba AuthService con mocks (sin BD real)
Integración: prueba Controller + Service + Repository + BD (H2 en memoria)

El test de integración detecta errores que los unitarios NO detectan:
- Configuración incorrecta de Spring Security
- Serialización/deserialización JSON incorrecta
- Validaciones @Valid que no funcionan
- Transacciones que no hacen commit
- Queries JPA con errores de sintaxis
```

### Test: `register_ValidData_Returns200`
```
¿Qué detecta?
  - Que el endpoint /api/auth/register existe y acepta POST
  - Que la serialización JSON funciona correctamente
  - Que Spring Security permite acceso sin token (permitAll)
  - Que la respuesta tiene la estructura esperada
  - Que el usuario se guarda en la BD (H2)
  - Que el token JWT se genera correctamente
```

### Test: `register_DuplicateUsername_Returns400`
```
¿Qué detecta?
  - Que el constraint UNIQUE funciona en la BD
  - Que el GlobalExceptionHandler captura la excepción
  - Que devuelve HTTP 400 (no 500)
  - Que el mensaje de error es claro para el frontend
```

### Test: `login_InvalidCredentials_Returns401`
```
¿Qué detecta?
  - Que Spring Security rechaza credenciales incorrectas
  - Que devuelve HTTP 401 (no 200 con error en body)
  - Que el JwtAuthEntryPoint maneja el error correctamente
```

---
