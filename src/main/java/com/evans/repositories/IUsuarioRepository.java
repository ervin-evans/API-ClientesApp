package com.evans.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evans.models.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	public Optional<Usuario> findByUsername(String username);
}
