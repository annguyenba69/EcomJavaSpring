package com.shop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired private CustomerService customerService;
	
	@PostMapping("/customers/checkUnique")
	public String checkUniqueEmail(@Param("email") String email) {
		return customerService.checkUniqueEmail(email) ? "Ok" : "Duplicated";
	}
	
}
