package com.fullstack.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Configuración de Redis como sistema de caché.
 * 
 * ¿Qué es Redis?
 * - Es una base de datos en memoria (key-value store).
 * - Es extremadamente rápida porque los datos viven en RAM.
 * - Se usa para: caché, sesiones, rate limiting, colas de mensajes.
 * 
 * ¿Por qué usarlo aquí?
 * 1. CACHÉ DE SESIONES: Evita consultar la BD en cada petición para validar el usuario.
 * 2. CACHÉ DE PRODUCTOS: Los productos no cambian cada segundo, se cachean 5 minutos.
 * 3. RATE LIMITING: En producción, los buckets se almacenarían en Redis (compartido entre instancias).
 * 4. BLACKLIST DE TOKENS: Cuando un usuario hace logout, su token se invalida en Redis.
 * 
 * ¿Cómo funciona el caché?
 * - Primera petición: se consulta la BD y se guarda el resultado en Redis.
 * - Siguientes peticiones: se devuelve directamente desde Redis (mucho más rápido).
 * - Después de X tiempo (TTL): el dato expira y se vuelve a consultar la BD.
 * 
 * Ejemplo:
 * - GET /api/products → Primera vez: consulta Oracle (200ms) → guarda en Redis
 * - GET /api/products → Segunda vez: lee de Redis (2ms) ← 100x más rápido
 * - Después de 5 min: el caché expira → vuelve a consultar Oracle
 * 
 * @EnableCaching activa las anotaciones @Cacheable, @CacheEvict, @CachePut
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    /**
     * Fábrica de conexiones a Redis.
     * Lettuce es el cliente Redis por defecto en Spring Boot (non-blocking, thread-safe).
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        return new LettuceConnectionFactory(config);
    }

    /**
     * Template para operaciones manuales con Redis.
     * Útil cuando necesitas control fino (guardar/leer/borrar claves específicas).
     * 
     * Ejemplo de uso:
     *   redisTemplate.opsForValue().set("token:blacklist:" + token, "revoked", Duration.ofHours(24));
     *   Boolean isBlacklisted = redisTemplate.hasKey("token:blacklist:" + token);
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Las claves se serializan como String (legibles en Redis CLI)
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Los valores se serializan como JSON (legibles y compatibles entre versiones)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Gestor de caché con Redis.
     * Define la configuración por defecto para todos los cachés:
     * - TTL (Time To Live): 10 minutos por defecto.
     * - No cachear valores null.
     * - Prefijo en las claves para evitar colisiones.
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))  // Los datos expiran después de 10 minutos
                .disableCachingNullValues()          // No guardar nulls en caché
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                // Cachés específicos con TTL personalizado
                .withCacheConfiguration("products",
                        defaultConfig.entryTtl(Duration.ofMinutes(5)))   // Productos: 5 min
                .withCacheConfiguration("users",
                        defaultConfig.entryTtl(Duration.ofMinutes(2)))   // Usuarios: 2 min
                .withCacheConfiguration("roles",
                        defaultConfig.entryTtl(Duration.ofMinutes(30)))  // Roles: 30 min (cambian poco)
                .build();
    }
}
