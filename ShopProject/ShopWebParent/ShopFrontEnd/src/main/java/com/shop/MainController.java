package com.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.banner.BannerService;
import com.shop.category.CategoryService;
import com.shop.common.entity.Banner;
import com.shop.common.entity.Category;
import com.shop.common.entity.Product;
import com.shop.product.ProductService;

@Controller
public class MainController {

	@Autowired private CategoryService categoryService;
	
	@Autowired private ProductService productService;
	
	@Autowired private BannerService bannerService;
	
	@GetMapping({"/", "/home"})
	public String viewHomePage(Model model) {
		List<Category> listCategoriesNoChildren = categoryService.listCategoriesNoChildren();
		model.addAttribute("listCategoriesNoChildren", listCategoriesNoChildren);
		List<Product> listFiveProductsTopRates = productService.getFiveProductsTopRates();
		model.addAttribute("listFiveProductsTopRates", listFiveProductsTopRates);
		List<Banner> banners = bannerService.findAll();
		model.addAttribute("banners", banners);
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLogin() {
		return "login";
	}
	
	@GetMapping("/error/404")
	public String error404() {
		return "error/404";
	}
	
}
