package com.modern.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Punto de entrada de la aplicación.
 *
 * @SpringBootApplication combina:
 * - @Configuration: esta clase puede definir @Bean
 * - @EnableAutoConfiguration: Spring configura automáticamente según las dependencias
 * - @ComponentScan: escanea este paquete y sub-paquetes buscando @Component, @Service, etc.
 *
 * @EnableCaching activa el soporte de caché (@Cacheable, @CacheEvict)
 */
@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
