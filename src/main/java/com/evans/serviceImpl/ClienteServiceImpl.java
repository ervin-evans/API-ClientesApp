package com.evans.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evans.models.Cliente;
import com.evans.repositories.IClienteRepository;
import com.evans.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteRepository iClienteRepository;

	@Override
	public List<Cliente> findAllClientes() {
		return iClienteRepository.findAll();
	}

	@Override
	public Cliente findById(Long id) {
		return iClienteRepository.findById(id).orElse(null);
	}

	@Override
	public Cliente save(Cliente cliente) {
		return iClienteRepository.save(cliente);
	}

	@Override
	public Cliente delete(Cliente cliente) {
		iClienteRepository.deleteById(cliente.getId());
		return cliente;
	}

}
