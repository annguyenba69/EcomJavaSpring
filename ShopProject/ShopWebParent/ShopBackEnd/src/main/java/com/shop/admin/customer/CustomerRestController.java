package com.shop.admin.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/customers/checkUniqueEmail")
	public String checkUniqueEmail(@Param("id") Integer id, @Param("email") String email) {
		return customerService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
}
