package com.evans.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evans.models.Usuario;
import com.evans.repositories.IUsuarioRepository;
import com.evans.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioRepository iUsuarioRepository;

	@Override
	public List<Usuario> findAll() {
		return iUsuarioRepository.findAll();
	}

	@Override
	public Page<Usuario> findByPage(Pageable pageable) {
		return iUsuarioRepository.findAll(pageable);
	}

	@Override
	public Usuario findById(Long id) {
		return iUsuarioRepository.findById(id).orElse(null);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return iUsuarioRepository.save(usuario);
	}

	@Override
	public Usuario delete(Usuario usuario) {
		iUsuarioRepository.delete(usuario);
		return usuario;
	}

	@Override
	public boolean userExist(String username) {
		return iUsuarioRepository.findByUsername(username).isPresent();
	}

	@Override
	public Usuario findByUsername(String username) {
		return iUsuarioRepository.findByUsername(username).orElse(null);
	}

}
