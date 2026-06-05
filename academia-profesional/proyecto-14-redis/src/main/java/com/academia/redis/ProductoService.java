package com.academia.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {
    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) { this.repo = repo; }

    @Cacheable(value = "productos", key = "'all'")
    public List<Producto> listar() {
        log.info("⚡ Consultando PostgreSQL (NO está en caché)");
        simulateSlowQuery();
        return repo.findByActivoTrue();
    }

    @Cacheable(value = "productos", key = "#id")
    public Producto buscarPorId(Long id) {
        log.info("⚡ Consultando PostgreSQL por ID={}", id);
        simulateSlowQuery();
        return repo.findById(id).orElseThrow(() -> new RuntimeException("No encontrado: " + id));
    }

    @Transactional
    @CacheEvict(value = "productos", allEntries = true)
    public Producto crear(Producto p) {
        log.info("💾 Creando producto + invalidando caché");
        return repo.save(p);
    }

    @Transactional
    @CacheEvict(value = "productos", allEntries = true)
    public void eliminar(Long id) {
        var p = buscarPorId(id);
        p.setActivo(false);
        repo.save(p);
        log.info("🗑️ Producto eliminado + caché invalidado");
    }

    private void simulateSlowQuery() {
        try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
