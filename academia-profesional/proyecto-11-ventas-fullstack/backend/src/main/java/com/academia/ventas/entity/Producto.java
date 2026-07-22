package com.academia.ventas.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    private int stock;

    @Column(unique = true)
    private String sku;

    @ManyToOne @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal p) { this.precio = p; }
    public int getStock() { return stock; }
    public void setStock(int s) { this.stock = s; }
    public String getSku() { return sku; }
    public void setSku(String s) { this.sku = s; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria c) { this.categoria = c; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
}
