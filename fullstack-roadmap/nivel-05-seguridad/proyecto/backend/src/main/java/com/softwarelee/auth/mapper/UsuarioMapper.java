package com.softwarelee.auth.mapper;

import com.softwarelee.auth.model.Rol;
import com.softwarelee.auth.model.Usuario;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsuarioMapper {

    // Buscar usuario por username (para login)
    Usuario findByUsername(@Param("username") String username);

    // Buscar usuario por ID
    Usuario findById(@Param("id") Long id);

    // Obtener roles de un usuario
    List<Rol> findRolesByUsuarioId(@Param("usuarioId") Long usuarioId);

    // Insertar nuevo usuario
    void insert(Usuario usuario);

    // Listar todos los usuarios activos
    List<Usuario> findAll();
}
