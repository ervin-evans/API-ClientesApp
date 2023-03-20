package com.evans.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.models.Producto;
import com.evans.service.IProductoService;

@RestController
@CrossOrigin(origins = { "http://localhost:4200" })
@RequestMapping("/productos")
public class ProductosController {

	@Autowired
	private IProductoService iProductoService;
	
	/**************************************************************************************************
	 * * MOSTRAR LA LISTA DE TODOS LOS PRODUCTOS
	 **************************************************************************************************/
	@GetMapping("/list")
	public List<Producto> findAll() {
		return iProductoService.findAll();
	}
	

}
