package com.shop.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Country;
import com.shop.common.entity.State;

@Service
public class StateService {

	@Autowired private StateRepository stateRepository;
	
	public List<State> findByCountryOrderByNameAsc(Country country){
		return stateRepository.findByCountryOrderByNameAsc(country);
	}
	
	public State save(State state) {
		return stateRepository.save(state);
	}
	
	public void delete(Integer id) {
		stateRepository.deleteById(id);
	}
	
}
