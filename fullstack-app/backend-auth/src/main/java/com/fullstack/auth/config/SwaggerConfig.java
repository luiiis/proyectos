package com.fullstack.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI 3.
 * 
 * ¿Qué es Swagger?
 * - Es una herramienta que genera documentación interactiva de tu API REST.
 * - Permite probar endpoints directamente desde el navegador.
 * - Genera un JSON/YAML con la especificación OpenAPI de tu API.
 * 
 * ¿Cómo acceder?
 * - UI interactiva: http://localhost:8080/swagger-ui.html
 * - JSON spec: http://localhost:8080/v3/api-docs
 * 
 * ¿Cómo funciona con JWT?
 * - Se configura un SecurityScheme tipo "bearer" (línea 55).
 * - En la UI de Swagger aparece un botón "Authorize" donde pegas tu token.
 * - Después de eso, todas las peticiones incluyen el header Authorization.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Nombre del esquema de seguridad (se referencia en SecurityRequirement)
        final String securitySchemeName = "Bearer JWT";

        return new OpenAPI()
                // Información general de la API
                .info(new Info()
                        .title("API Sistema de Gestión")
                        .version("1.0.0")
                        .description("""
                                API REST para el sistema de gestión fullstack.
                                
                                ## Autenticación
                                1. Usa `/api/auth/login` para obtener un token JWT
                                2. Clic en "Authorize" (candado arriba a la derecha)
                                3. Pega el token con formato: `Bearer tu-token-aqui`
                                4. Ahora puedes usar los endpoints protegidos
                                
                                ## Roles
                                - **ADMIN**: Acceso total
                                - **MANAGER**: Gestión de productos e inventario
                                - **USER**: Solo lectura y perfil propio
                                """)
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@sistema.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))

                // Agregar el requisito de seguridad global (JWT en todos los endpoints)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // Definir CÓMO se envía el token (header Authorization con prefijo Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Pega aquí tu JWT token (sin el prefijo 'Bearer ')")
                        ));
    }
}
