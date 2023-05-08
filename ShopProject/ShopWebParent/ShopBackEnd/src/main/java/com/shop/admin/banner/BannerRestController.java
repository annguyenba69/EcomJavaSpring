package com.shop.admin.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BannerRestController {

	@Autowired private BannerService bannerService;
	
	@PostMapping("/banners/checkUnique")
	public String checkUnique(@Param("id") Integer id, @Param("title") String title) {
		return bannerService.checkUniqueTitle(id, title) ? "Ok" : "DuplicatedTitle";
	}
	
}
