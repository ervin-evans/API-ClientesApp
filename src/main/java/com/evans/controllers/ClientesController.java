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
import com.evans.models.Cliente;
import com.evans.service.IClienteService;

import jakarta.validation.Valid;

@CrossOrigin(origins = { "http://localhost:4200" }) // para ser consumido desde una Fron-End con Angular
@RestController
@RequestMapping("/clientes")
public class ClientesController {

	@Autowired
	private IClienteService iClienteService;

	@Value("${clientes.app.path.images}")
	private String pathImages;

	/**************************************************************************************************
	 * * MOSTRAR TODOS LOS CLIENTES
	 **************************************************************************************************/

	@GetMapping("/list")
	public List<Cliente> clientes() {
		return iClienteService.findAllClientes();
	}

	/**************************************************************************************************
	 * * BUSCAR CLIENTE POR ID
	 **************************************************************************************************/

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			cliente = iClienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al consultar con la base de datos");
			response.put("errors", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("msg", "El cliente con el id: " + id + " no se encontro en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
		}

	}

	/**************************************************************************************************
	 * * GUARDAR UN CLIENTE
	 **************************************************************************************************/

	@PostMapping("/save")
	public ResponseEntity<?> save(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteDb = null;
		System.out.println(cliente);
		Map<String, Object> response = new HashMap<String, Object>();
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errors.add(e.getDefaultMessage());
			});
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteDb = iClienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo errores al gurdar el cliente en la base de datos porque ");
			response.put("errors", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("msg", "El cliente " + clienteDb.getNombre() + " " + clienteDb.getApellidoPaterno() + " "
				+ clienteDb.getApellidoMaterno() + " fue guardado satisfactoriamente");
		response.put("cliente", clienteDb);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/**************************************************************************************************
	 * * ACTUALIZAR UN CLIENTE
	 **************************************************************************************************/
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result,
			@PathVariable("id") Long id) {
		Cliente clienteDB = null;
		Map<String, Object> response = new HashMap<String, Object>();
		clienteDB = iClienteService.findById(id);
		// verificando los errores de validacion
		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errors.add(e.getDefaultMessage());
			});
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if (clienteDB == null) {
			response.put("msg", "No se encontro el cliente con el id " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		List<Cliente> clientesByEmail = iClienteService.findByEmail(cliente.getEmail());
		if (!clientesByEmail.isEmpty()) {
			response.put("msg", "El email " + cliente.getEmail() + " ya existe");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteDB.setNombre(cliente.getNombre());
			clienteDB.setApellidoPaterno(cliente.getApellidoMaterno());
			clienteDB.setApellidoPaterno(cliente.getApellidoMaterno());
			clienteDB.setEmail(cliente.getEmail());
			clienteDB.setFoto(cliente.getFoto());
			clienteDB.setRegion(cliente.getRegion());
			clienteDB = iClienteService.save(clienteDB);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo algunos errores al actualizar el registro");
			response.put("errors", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("msg", "El cliente " + clienteDB.getNombre() + " " + clienteDB.getApellidoPaterno() + ' '
				+ clienteDB.getApellidoMaterno() + " fue actualizado satisfactoriamente");
		response.put("cliente", clienteDB);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	/**************************************************************************************************
	 * * ELIMINAR UN CLIENTE
	 **************************************************************************************************/
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<String, Object>();
		Cliente cliente = null;
		try {
			// buscamos el id del cliente para verificar si existe en nuestra base de datos
			cliente = iClienteService.findById(id);
			// si el cliente no existe
			if (cliente == null) {
				response.put("msg", "No existe cliente con id " + id);
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			cliente = iClienteService.delete(cliente);
		} catch (DataAccessException e) {
			response.put("msg", "Hubo errores al eliminar el cliente de la base de datos");
			response.put("errors", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("msg", "El cliente " + cliente.getNombre() + " " + cliente.getApellidoPaterno() + " "
				+ cliente.getApellidoMaterno() + " fue eliminado");
		response.put("cliente", cliente);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	/**************************************************************************************************
	 * * GUARDAR IMAGENES DEL CLIENTE
	 **************************************************************************************************/

	@PostMapping("/image/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		if (UploadPhoto.isValidImageExtension(file.getOriginalFilename())) {
			Cliente cliente = iClienteService.findById(id);
			if (cliente != null) {
				String imageSaved = UploadPhoto.savePhoto(file, pathImages);
				if (imageSaved != null) {
					cliente.setFoto(imageSaved);
					cliente = iClienteService.save(cliente);
					response.put("msg",
							"La foto del cliente " + cliente.getNombre() + " " + cliente.getApellidoPaterno() + " "
									+ cliente.getApellidoMaterno() + " ha sido guardado satisfactoriamente");
					response.put("cliente", cliente);
				} else {
					response.put("errors", "Hubo un error al guardar la imagen del cliente");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response.put("msg", "El cliente con el id " + id + " no pudo ser encontrado");
			}
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

		} else {
			response.put("msg", "Debe cargar una imagen ['PNG','JPEG','JPG']");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
	}

	/**************************************************************************************************
	 * * RECUPERAR LA IMAGE DEL CLIENTE
	 **************************************************************************************************/
	@GetMapping("/image/uploads/{imageName:.+}")
	public ResponseEntity<Resource> showImage(@PathVariable("imageName") String imageName) {
		Resource resource = null;
		try {
			System.out.println("ruta de las imagenes" + pathImages + imageName);
			resource = new UrlResource("file", pathImages + imageName);
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
