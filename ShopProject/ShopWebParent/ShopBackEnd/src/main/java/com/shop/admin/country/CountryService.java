package com.shop.admin.country;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Country;

@Service
public class CountryService {

	@Autowired private CountryRepository countryRepository;
	
	public List<Country> listAll(){
		return (List<Country>) countryRepository.findAll();
	}
	
	public Country save(Country country) {
		return countryRepository.save(country);
	}
	
	public void delete(Integer id) throws CountryRestNotFoundException {
		long countById = countryRepository.countById(id);
		if(countById == 0) {
			throw new CountryRestNotFoundException();
		}
		countryRepository.deleteById(id);
	}
	
	public Country get(Integer id) {
		return countryRepository.findById(id).get();
	}
	
}
