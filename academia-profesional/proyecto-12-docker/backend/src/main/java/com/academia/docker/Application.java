package com.academia.docker;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Proyecto 12: App dockerizada con PostgreSQL + Redis
 * Demuestra: Multi-stage build, health checks, caché Redis, deploy con Docker Compose
 */
@SpringBootApplication
@EnableCaching
public class Application {
    public static void main(String[] args) { SpringApplication.run(Application.class, args); }

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        var config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10))
            .disableCachingNullValues();
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }
}

@Entity @Table(name = "productos")
class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    String nombre;
    BigDecimal precio;
    int stock;
    boolean activo = true;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { nombre = n; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal p) { precio = p; }
    public int getStock() { return stock; }
    public void setStock(int s) { stock = s; }
    public boolean isActivo() { return activo; }
}

interface ProductoRepo extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
}

@RestController @RequestMapping("/api/productos")
class ProductoController {
    private final ProductoRepo repo;
    ProductoController(ProductoRepo r) { this.repo = r; }

    @GetMapping
    @Cacheable("productos")
    public List<Producto> listar() { return repo.findByActivoTrue(); }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    @CacheEvict(value = "productos", allEntries = true)
    public Producto crear(@RequestBody Producto p) { return repo.save(p); }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "productos", allEntries = true)
    public void eliminar(@PathVariable Long id) {
        var p = repo.findById(id).orElseThrow();
        p.setActivo(false);
        repo.save(p);
    }
}

@RestController
class HealthController {
    @GetMapping("/api/info")
    public Map<String, String> info() {
        return Map.of(
            "app", "Docker Demo",
            "version", "1.0.0",
            "java", System.getProperty("java.version"),
            "container", System.getenv("HOSTNAME") != null ? System.getenv("HOSTNAME") : "local"
        );
    }
}
