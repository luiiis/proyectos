package com.academia.graphql.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String nombre;
    private String descripcion;
    @Column(nullable = false) private double precio;
    private int stock;
    private String categoria;
    @Column(unique = true) private String sku;
    private boolean activo = true;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String d) { this.descripcion = d; }
    public double getPrecio() { return precio; }
    public void setPrecio(double p) { this.precio = p; }
    public int getStock() { return stock; }
    public void setStock(int s) { this.stock = s; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { this.categoria = c; }
    public String getSku() { return sku; }
    public void setSku(String s) { this.sku = s; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean a) { this.activo = a; }
}
