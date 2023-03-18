package com.evans.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.models.Cliente;
import com.evans.service.IClienteService;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

	@Autowired
	private IClienteService iClienteService;

	@GetMapping("/list")
	public List<Cliente> clientes() {
		return iClienteService.findAllClientes();
	}

	

}
