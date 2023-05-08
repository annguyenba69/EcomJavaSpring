package com.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.common.entity.Customer;
import com.shop.customer.CustomerNotFoundException;
import com.shop.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Helper {
	
	@Autowired private CustomerService customerService;
	
	public Customer getCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findByEmail(email);
		return customer;
	}
	
}
