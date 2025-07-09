package com.desarrolloweb.comercial.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desarrolloweb.comercial.model.entity.Producto;

@Repository
public interface ProductoDAOIface extends JpaRepository<Producto, Long> {

  @Query("select p from Producto p where p.descripcion like %?1%")
  public List<Producto> buscarPorDescripcion(String term);

  public List<Producto> findByDescripcionLikeIgnoreCase(String term);    
}
