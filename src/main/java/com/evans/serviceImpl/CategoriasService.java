package com.evans.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evans.models.Categoria;
import com.evans.repositories.ICategoriasRepository;
import com.evans.service.ICategoriaService;

@Service
public class CategoriasService implements ICategoriaService {

	@Autowired
	private ICategoriasRepository iCategoriasRepository;

	@Override
	public List<Categoria> findAll() {
		return (List<Categoria>) iCategoriasRepository.findAll();
	}

}
