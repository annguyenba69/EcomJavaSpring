package com.shop.wishlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.Helper;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.customer.CustomerNotFoundException;
import com.shop.product.ProductNotFoundException;
import com.shop.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WishListRestController {

	@Autowired private WishListService wishListService;
	@Autowired private ProductService productService;
	@Autowired private Helper helper;
	
	@PostMapping("/wishlists/add/{productId}")
	public String addWishList(@PathVariable(name = "productId") Integer id, HttpServletRequest request){
		try {
			Customer customer = helper.getCustomer(request);
			return wishListService.save(id, customer);
		}catch(CustomerNotFoundException ex) {
			return "You must login to continue add wishlist";
		}catch(ProductNotFoundException ex) {
			return ex.getMessage();
		}
	}
	
}
