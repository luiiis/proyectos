package com.academia.ventas.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String email;
    private String nombre;

    @ManyToOne @JoinColumn(name = "rol_id")
    private Rol rol;

    private boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // UserDetails implementation
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
    }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return activo; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return activo; }

    // Getters & Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public void setUsername(String u) { this.username = u; }
    public void setPasswordHash(String p) { this.passwordHash = p; }
    public void setNombre(String n) { this.nombre = n; }
    public void setRol(Rol r) { this.rol = r; }
    public void setEmail(String e) { this.email = e; }
}
