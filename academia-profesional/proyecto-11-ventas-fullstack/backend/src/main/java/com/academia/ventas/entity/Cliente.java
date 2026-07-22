package com.academia.ventas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    @Column(unique = true) private String email;
    private String telefono;
    private String ciudad;
    private String tipo = "REGULAR";
    private boolean activo = true;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }
    public String getApellido() { return apellido; }
    public void setApellido(String a) { this.apellido = a; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getTelefono() { return telefono; }
    public String getCiudad() { return ciudad; }
    public String getTipo() { return tipo; }
    public boolean isActivo() { return activo; }
}
