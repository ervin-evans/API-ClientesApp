package com.evans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evans.models.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
	
}
