package com.evans.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.service.ICategoriaService;
import com.evans.service.IProductoService;

@RestController
@CrossOrigin(origins = { "http://localhost:4200" })
@RequestMapping("/productos")
public class ProductosController {

	@Autowired
	private IProductoService iProductoService;

	@Autowired
	private ICategoriaService iCategoriaService;

	private Map<String, Object> response = null;

	/**************************************************************************************************
	 * * MOSTRAR LA LISTA DE TODOS LOS PRODUCTOS
	 **************************************************************************************************/
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> findAll() {
		response = new HashMap<>();
		response.put("productos", iProductoService.findAll());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/**************************************************************************************************
	 * * MOSTRAR LA LISTA DE TODAS LAS CATEGORIAS DE LOS PRODUCTOS
	 **************************************************************************************************/
	@GetMapping("/categorias/list")
	public ResponseEntity<Map<String, Object>> findAllCategorias() {
		response = new HashMap<>();
		response.put("categorias", iCategoriaService.findAll());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	/**************************************************************************************************
	 * * MOSTRAR LA LISTA DE PRODUCTOS CON PAGINACION
	 **************************************************************************************************/
	@GetMapping("/paginated")
	public ResponseEntity<Map<String, Object>> findAllPaginated(Pageable pageable) {
		response = new HashMap<>();
		response.put("ProductosPaginated", iProductoService.findAll(pageable));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
