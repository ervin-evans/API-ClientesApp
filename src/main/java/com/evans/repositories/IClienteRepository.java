package com.evans.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evans.models.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {
	public List<Cliente> findByEmail(String email);

}
