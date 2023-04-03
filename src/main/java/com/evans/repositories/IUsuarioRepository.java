package com.evans.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evans.models.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	public List<Usuario> findByUsername(String username);
}
