package com.shop.state;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.country.CountryService;
import com.shop.common.entity.Country;
import com.shop.common.entity.State;

@RestController
public class StateRestController {

	@Autowired private CountryService countryService;
	
	@Autowired private StateService stateService;
	
	@GetMapping("/states/listStateByCountry/{id}")
	public List<StateDTO> listStateByCountry(@PathVariable(name = "id") Integer id){
		List<StateDTO> stateDTOs = new ArrayList<>();
		Country country = countryService.get(id);
		List<State> states = stateService.findByCountryOrderByNameAsc(country);
		for(State state : states) {
			stateDTOs.add(new StateDTO(state.getId(), state.getName()));
		}
		return stateDTOs;
	}

	
}
