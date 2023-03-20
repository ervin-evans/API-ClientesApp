package com.evans.service;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evans.models.Producto;

public interface IProductoService {

	public List<Producto> findAll();

	public List<Producto> findByExample(Example<Producto> example);

	public Page<Producto> findAll(Pageable pageable);

	public Producto findByid(Long id);

	public Producto save(Producto producto);

	public Producto delete(Producto producto);

}
