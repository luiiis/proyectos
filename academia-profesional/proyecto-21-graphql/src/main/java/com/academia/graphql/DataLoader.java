package com.academia.graphql;

import com.academia.graphql.entity.Producto;
import com.academia.graphql.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository repo;
    public DataLoader(ProductoRepository repo) { this.repo = repo; }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            crear("Laptop HP ProBook", 18999, 25, "Laptops", "LAP-001");
            crear("MacBook Air M3", 24999, 15, "Laptops", "MAC-001");
            crear("Monitor Dell 27\"", 12499, 20, "Monitores", "MON-001");
            crear("Teclado MX Keys", 2899, 40, "Perifericos", "TEC-001");
            crear("Mouse MX Master", 1899, 50, "Perifericos", "MOU-001");
            crear("Silla Herman Miller", 32500, 8, "Mobiliario", "SIL-001");
            crear("SSD Samsung 1TB", 2199, 30, "Almacenamiento", "SSD-001");
            crear("Webcam Elgato 4K", 5999, 20, "Perifericos", "WEB-001");
            System.out.println("✓ 8 productos cargados");
        }
    }

    private void crear(String nombre, double precio, int stock, String cat, String sku) {
        var p = new Producto();
        p.setNombre(nombre); p.setPrecio(precio); p.setStock(stock);
        p.setCategoria(cat); p.setSku(sku);
        repo.save(p);
    }
}
