package com.shop.admin.country;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.common.entity.Country;

@RestController
public class CountryRestController {

	@Autowired private CountryService countryService;
	
	@GetMapping("/countries/listAll")
	public List<CountryDTO> listAll() {
		List<CountryDTO> countryDTOs = new ArrayList<>();
		List<Country> countries = countryService.listAll();
		for(Country country : countries) {
			countryDTOs.add(new CountryDTO(country.getId(), country.getName(), country.getCode()));
		}
		return countryDTOs;
	}
	
	@PostMapping("/countries/save")
	public String saveCountry(@RequestBody Country country) {
		Country savedCountry = countryService.save(country);
		return String.valueOf(savedCountry.getId());
	}
	
	@DeleteMapping("/countries/delete/{id}")
	public String delete(@PathVariable(name = "id") Integer id) {
		try {
			countryService.delete(id);
			return "Delete Success";
		}catch(CountryRestNotFoundException ex) {
			return ex.getMessage();
		}
	}
}
