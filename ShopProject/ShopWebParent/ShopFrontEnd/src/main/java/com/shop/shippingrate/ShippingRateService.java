package com.shop.shippingrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Address;
import com.shop.common.entity.Country;
import com.shop.common.entity.Customer;
import com.shop.common.entity.ShippingRate;

@Service
public class ShippingRateService {

	@Autowired private ShippingRateRepository shippingRateRepository;
	
	public ShippingRate  getShippingRateByCustomer(Customer customer) {
		String state = customer.getState();
		if(state == null || state.isEmpty()) {
			state = customer.getCity();
		}
		return shippingRateRepository.findByCountryAndState(customer.getCountry(), state);
	}
	
	public ShippingRate getShippingRateByAddress(Address address) {
		String state = address.getState();
		if(state == null || state.isEmpty()) {
			state = address.getCity();
		}
		return shippingRateRepository.findByCountryAndState(address.getCountry(), state);
	}
	
}
