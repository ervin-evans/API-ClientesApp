package com.evans.repositories;

import org.springframework.data.repository.CrudRepository;

import com.evans.models.Categoria;

public interface ICategoriasRepository extends CrudRepository<Categoria, Integer> {

}
