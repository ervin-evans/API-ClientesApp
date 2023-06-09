package com.evans.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evans.models.Producto;
import com.evans.models.Usuario;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
	
}
