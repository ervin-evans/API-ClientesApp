package com.evans.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evans.models.Region;
import com.evans.repositories.IRegionesRepositry;
import com.evans.service.IRegionesService;

@Service
public class RegionesServiceImpl implements IRegionesService {

	@Autowired
	private IRegionesRepositry iRegionesRepositry;

	@Override
	public List<Region> findAll() {
		return (List<Region>) iRegionesRepositry.findAll();
	}

}
