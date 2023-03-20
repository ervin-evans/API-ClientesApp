package com.evans.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evans.models.Producto;
import com.evans.service.ICategoriaService;
import com.evans.service.IProductoService;

import jakarta.validation.Valid;

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

	/**************************************************************************************************
	 * * BUSCAR UN PRODUCTO POR ID
	 **************************************************************************************************/

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> findProductoById(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Producto producto = null;
		try {
			producto = iProductoService.findByid(id);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al buscar el producto");
			response.put("error", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (producto == null) {
			response.put("msg", "No pudimos encontrar el producto con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		response.put("producto", producto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

}
