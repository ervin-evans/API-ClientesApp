package com.evans.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.evans.models.Usuario;

public interface IUsuarioService {

	public List<Usuario> findAll();

	public Usuario findById(Long id);

	public Usuario findByUsername(String username);
	public boolean userExist(String username);

	public Page<Usuario> findByPage(Pageable pageable);

	public Usuario save(Usuario usuario);

	public Usuario delete(Usuario usuario);

}
