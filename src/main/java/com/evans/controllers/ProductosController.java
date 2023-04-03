package com.evans.controllers;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.evans.helpers.UploadPhoto;
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

	@Value("${clientes.app.path.productos.images}")
	private String pathProductosImage;

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

	@GetMapping("/find/{id}")
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

	/**************************************************************************************************
	 * * GUARGAR PRODUCTO
	 **************************************************************************************************/
	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Producto producto, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Producto productoDb = null;
		// verificamos errores de validacion
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errors.add(e.getDefaultMessage());
			});
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		// intentamos guardar el producto
		try {
			productoDb = iProductoService.save(producto);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al guardar el producto");
			response.put("error", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("msg", "El producto " + productoDb.getNombre() + "ha sido guardado satisfactoriamente!");
		response.put("producto", productoDb);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/**************************************************************************************************
	 * * ACTUALIZAR UN PRODUCTO
	 **************************************************************************************************/
	@PutMapping("/update")
	public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Producto producto, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Producto productoDb = null;
	
		// verificamos errores de validacion
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errors.add(e.getDefaultMessage());
			});
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			// buscamos el producto que queremos actualizar
			productoDb = iProductoService.findByid(producto.getId());
			if (productoDb == null) {
				response.put("msg", "Error al actualizar el producto porque no pudimos encontrar el producto con el id "
						+ producto.getId());
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			// establecemos los nuevos valores
			productoDb.setNombre(producto.getNombre());
			productoDb.setDescripcion(producto.getDescripcion());
			productoDb.setPrecio(producto.getPrecio());
			productoDb.setStock(producto.getStock());
			productoDb.setCreatedAt(productoDb.getCreatedAt());
			productoDb.setCategoria(producto.getCategoria());
			// guardamos el producto con los nuevos valores
			productoDb = iProductoService.save(productoDb);

		} catch (DataAccessException e) {
			response.put("msg", "Hubo errores al intentar actualizar el producto");
			response.put("errors", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("msg", "El producto " + productoDb.getNombre() + " ha sido actualizado satisfactoriamente");
		response.put("producto", productoDb);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/**************************************************************************************************
	 * * ELIMINAR UN PRODUCTO
	 **************************************************************************************************/
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Producto producto = null;
		try {
			producto = iProductoService.findByid(id);
			if (producto == null) {
				response.put("msg", "No pudimos eliminar el producto porque no existe el id proporcionado");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			iProductoService.delete(producto);
		} catch (DataAccessException e) {
			response.put("msg", "No pudimos eliminar el producto");
			response.put("errors", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("msg", "El producto " + producto.getNombre() + " ha sido eliminado satisfactoriamente!");
		response.put("producto", producto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**************************************************************************************************
	 * * SUBIR IMAGEN DE UN PRODUCTO
	 **************************************************************************************************/
	@PostMapping("/image/upload")
	public ResponseEntity<Map<String, Object>> saveImage(@RequestParam("image") MultipartFile file,
			@RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		if (UploadPhoto.isValidImageExtension(file.getOriginalFilename())) {
			Producto producto = iProductoService.findByid(id);
			if (producto != null) {
				String imageProductoName = UploadPhoto.savePhoto(file, pathProductosImage);
				if (imageProductoName != null) {
					producto.setImage(imageProductoName);
					response.put("msg", "La imagen se ha guardado satisafactoriamente");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
				} else {
					response.put("msg", "No pudimos guardar la imagen porque ha ocurrido un error con el servidor");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response.put("msg", "No pudimos guardar la imagen porque no existe el producto");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
		} else {
			response.put("msg", "Debe subir una imagen con formato valido[PNG, JPG, JPEG]");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**************************************************************************************************
	 * * RECUPERAR LA IMAGE DEL PRODUCTO
	 **************************************************************************************************/
	@GetMapping("/image/uploads/{imageName:.+}")
	public ResponseEntity<Resource> showImage(@PathVariable("imageName") String imageName) {
		Resource resource = null;
		try {
			resource = new UrlResource("file", pathProductosImage + imageName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		if (!resource.exists() && resource.isReadable()) {
			throw new RuntimeException("Error, no se pudo cargar la imagen: " + imageName);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "image/*");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}

}
