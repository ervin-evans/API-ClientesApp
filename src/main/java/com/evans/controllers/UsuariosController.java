package com.evans.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import com.evans.models.Usuario;
import com.evans.service.IUsuarioService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = { "http://localhost:4200" })
@RequestMapping("/users")
public class UsuariosController {

	@Autowired
	private IUsuarioService iUsuarioService;
	/**************************************************************************************************
	 * * BUSCAR LA LISTA DE TODOS LOS USUARIOS
	 **************************************************************************************************/
	@GetMapping("/list")
	public List<Usuario> findAll() {
		return iUsuarioService.findAll();
	}
	
	/**************************************************************************************************
	 * * BUSCAR USUARIOS DE MANERA PAGINADA
	 **************************************************************************************************/

	@GetMapping("/paginated")
	public Page<Usuario> findByPage(Pageable pageable) {
		return iUsuarioService.findByPage(pageable);
	}
	
	/**************************************************************************************************
	 * * BUSCAR USUARIO POR ID
	 **************************************************************************************************/

	@GetMapping("/find/{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Usuario usuario = iUsuarioService.findById(id);
		if (usuario != null) {
			response.put("usuario", usuario);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			response.put("msg", "No existe usuario con el id proporcionado");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

	}
	/**************************************************************************************************
	 * * PERSISTIR USUARIO EN DB
	 **************************************************************************************************/

	@PostMapping("/save")
	public ResponseEntity<Map<String, Object>> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		// Chequeamos los errores de validacion

		if (result.hasErrors()) {
			List<String> errores = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errores.add(e.getDefaultMessage());
			});
			response.put("errors", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		// intentamos guardar el registro del usuario
		try {
			boolean usernameExist = iUsuarioService.userExist(usuario.getUsername());
			if (usernameExist) {
				response.put("msg", "El usuario " + usuario.getUsername() + " ya existe");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			}
			Usuario usuarioDb = iUsuarioService.save(usuario);
			response.put("msg", "El usuario " + usuarioDb.getNombre() + " " + usuarioDb.getApellidoPaterno() + " "
					+ usuarioDb.getApellidoMaterno() + " fue guardado satisfactoriamente");
		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al intentar guardar el usuario");
			response.put("error", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/**************************************************************************************************
	 * * EDITAR USUARIO
	 **************************************************************************************************/
	@PutMapping("/update")
	public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody Usuario usuario, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errores = new ArrayList<>();
			result.getFieldErrors().forEach(e -> {
				errores.add(e.getDefaultMessage());
			});
			response.put("errors", errores);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		// verificamos que el nuevo usuario no exista en la base de datos
		Usuario usuarioDb = iUsuarioService.findByUsername(usuario.getUsername());
		if (usuarioDb != null && usuario.getId() != usuarioDb.getId()) {
			response.put("msg", "El usuario " + usuario.getUsername() + " ya existe");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		// intentamos actualizar al usuario
		try {
			Usuario usuarioSaved = iUsuarioService.save(usuario);
			response.put("msg", "El usuario " + usuarioSaved.getNombre() + " " + usuario.getApellidoPaterno() + " "
					+ usuarioSaved.getApellidoMaterno() + " ha sido actualizado con exito!!!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al intentar actualizar al usuario");
			response.put("error", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**************************************************************************************************
	 * * ELIMINAR USUARIO
	 **************************************************************************************************/
	@DeleteMapping("/delete")
	public ResponseEntity<Map<String, Object>> delete(@RequestBody Usuario usuario) {
		Map<String, Object> response = new HashMap<>();
		try {
			iUsuarioService.delete(usuario);
			response.put("msg", "El usuario " + usuario.getNombre() + " " + usuario.getApellidoPaterno() + " "
					+ usuario.getApellidoMaterno() + " fue eliminado exitosamente!!!");

		} catch (DataAccessException e) {
			response.put("msg", "Hubo un error al intentar eliminar al usuario");
			response.put("error", e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}


}
