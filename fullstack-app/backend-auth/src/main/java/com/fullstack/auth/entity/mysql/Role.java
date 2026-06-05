package com.fullstack.auth.entity.mysql;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Rol almacenada en MySQL.
 * Define los roles del sistema: ADMIN, USER, MANAGER, etc.
 */
@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @Column(length = 100)
    private String description;
}
