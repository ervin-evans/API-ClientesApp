package com.evans.service;

import java.util.List;

import com.evans.models.Cliente;

public interface IClienteService {
	public List<Cliente> findAllClientes();

	public Cliente findById(Long id);

	public List<Cliente> findByEmail(String email);

	public Cliente save(Cliente cliente);

	public Cliente delete(Cliente cliente);
}
