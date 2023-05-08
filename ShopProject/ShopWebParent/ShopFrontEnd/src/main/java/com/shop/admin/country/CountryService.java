package com.shop.admin.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Country;

@Service
public class CountryService {

	@Autowired CountryRepository countryRepository;
	
	public List<Country> listAll(){
		return (List<Country>) countryRepository.findAll();
	}
	
	public Country get(Integer id) {
		return countryRepository.findById(id).get();
	}
}
