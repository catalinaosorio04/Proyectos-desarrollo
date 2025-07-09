package com.desarrolloweb.comercial.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.desarrolloweb.comercial.model.entity.Usuario;

public interface UsuarioDAOIface extends JpaRepository<Usuario, Long> {

    public Usuario findByNombre(String nombre);

}
