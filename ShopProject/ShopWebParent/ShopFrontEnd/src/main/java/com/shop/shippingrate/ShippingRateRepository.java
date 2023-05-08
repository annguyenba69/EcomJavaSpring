package com.shop.shippingrate;

import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Country;
import com.shop.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
	
}
