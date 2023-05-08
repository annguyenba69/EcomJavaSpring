package com.shop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

	@Autowired private UserService userService;
	
	@PostMapping("/users/checkUniqueEmail")
	public String checkUniqueEmail(@Param("id") Integer id, @Param("email") String email) {
		return userService.checkUniqueEmail(id, email) ? "Ok" : "Duplicated";
	}
	
}
