package com.evans.service;

import java.util.List;

import com.evans.models.Producto;

public interface IProductoService {

	public List<Producto> findAll();

	public Producto findByid(Long id);

	public Producto save(Producto producto);

	public Producto delete(Producto producto);

}
