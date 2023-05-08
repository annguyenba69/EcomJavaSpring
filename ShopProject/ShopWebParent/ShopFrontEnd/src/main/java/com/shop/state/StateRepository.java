package com.shop.state;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Country;
import com.shop.common.entity.State;

public interface StateRepository extends CrudRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc(Country country);
	
}
