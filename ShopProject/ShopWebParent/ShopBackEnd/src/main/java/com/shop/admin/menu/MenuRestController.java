package com.shop.admin.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuRestController {

	@Autowired private MenuService menuService;
	
	@PostMapping("/menus/checkUnique")
	public String checkUnique(@Param("id") Integer id, @Param("title") String title, @Param("alias") String alias) {
		return menuService.checkUnique(id, title, alias);
	}
	
}
