package com.evans.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.models.Cliente;
import com.evans.service.IClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

	@Autowired
	private IClienteService iClienteService;

	@GetMapping("/list")
	public List<Cliente> clientes() {
		return iClienteService.findAllClientes();
	}

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

}
