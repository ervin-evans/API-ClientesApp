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

	@GetMapping("/list")
	public List<Usuario> findAll() {
		return iUsuarioService.findAll();
	}

	@GetMapping("/paginated")
	public Page<Usuario> findByPage(Pageable pageable) {
		return iUsuarioService.findByPage(pageable);
	}

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
	//TODO:validar que el usuario ya esta en la base de datos
	

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
		//intentamos guardar el registro del usuario
		try {
			List<Usuario> listaUsername = iUsuarioService.findByUsername(usuario.getUsername());
			if (listaUsername.size() >= 1) {
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
	
	
	
	

}
