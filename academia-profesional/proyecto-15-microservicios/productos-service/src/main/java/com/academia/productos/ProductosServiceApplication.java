package com.academia.productos;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class ProductosServiceApplication {
    public static void main(String[] args) { SpringApplication.run(ProductosServiceApplication.class, args); }
}

@Entity @Table(name = "productos")
class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    String nombre;
    BigDecimal precio;
    int stock;
    String sku;
    boolean activo = true;
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal p) { this.precio = p; }
    public int getStock() { return stock; }
    public void setStock(int s) { this.stock = s; }
    public String getSku() { return sku; }
    public void setSku(String s) { this.sku = s; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
}

interface ProductoRepo extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
}

@RestController @RequestMapping("/api/productos")
class ProductoController {
    private final ProductoRepo repo;
    ProductoController(ProductoRepo r) { this.repo = r; }

    @GetMapping List<Producto> listar() { return repo.findByActivoTrue(); }
    @GetMapping("/{id}") Producto buscar(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
    @PostMapping Producto crear(@RequestBody Producto p) { return repo.save(p); }
    @DeleteMapping("/{id}") void eliminar(@PathVariable Long id) { var p = repo.findById(id).orElseThrow(); p.setActivo(false); repo.save(p); }
}
