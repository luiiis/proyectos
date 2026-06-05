package com.erp.service;

import com.erp.entity.Producto;
import com.erp.repository.ProductoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoService {
    private final ProductoRepository repo;
    public ProductoService(ProductoRepository repo) { this.repo = repo; }

    @Cacheable(value = "productos", key = "'page:'+#pageable.pageNumber")
    public Page<Producto> listar(Pageable pageable) { return repo.findByActivoTrue(pageable); }

    public Producto buscarPorId(Long id) { return repo.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id)); }

    public List<Producto> buscar(String nombre) { return repo.findByNombreContainingIgnoreCase(nombre); }

    public List<Producto> stockBajo(int min) { return repo.findStockBajo(min); }

    @Transactional @CacheEvict(value = "productos", allEntries = true)
    public Producto crear(Producto p) { return repo.save(p); }

    @Transactional @CacheEvict(value = "productos", allEntries = true)
    public Producto actualizar(Long id, Producto datos) {
        var p = buscarPorId(id);
        if (datos.getNombre() != null) p.setNombre(datos.getNombre());
        if (datos.getPrecio() != null) p.setPrecio(datos.getPrecio());
        if (datos.getDescripcion() != null) p.setDescripcion(datos.getDescripcion());
        if (datos.getSku() != null) p.setSku(datos.getSku());
        return repo.save(p);
    }

    @Transactional @CacheEvict(value = "productos", allEntries = true)
    public void eliminar(Long id) { var p = buscarPorId(id); p.setActivo(false); repo.save(p); }
}
