package com.evans.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evans.models.Producto;
import com.evans.repositories.IProductoRepository;
import com.evans.service.IProductoService;

@Service
public class ProductosServiceImpl implements IProductoService {

	@Autowired
	private IProductoRepository iProductoRepository;

	@Override
	public List<Producto> findAll() {
		return iProductoRepository.findAll();
	}

	@Override
	public Producto findByid(Long id) {
		return iProductoRepository.findById(id).orElse(null);
	}

	@Override
	public Producto save(Producto producto) {
		return iProductoRepository.save(producto);
	}

	@Override
	public Producto delete(Producto producto) {
		iProductoRepository.delete(producto);
		return producto;
	}

}
