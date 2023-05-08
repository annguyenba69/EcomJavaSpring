package com.shop.admin.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.Utility;
import com.shop.common.entity.Customer;
import com.shop.customer.CustomerNotFoundException;
import com.shop.customer.CustomerService;
import com.shop.product.ProductNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId") Integer productId, @PathVariable("quantity") Integer quantity,
			HttpServletRequest request) throws ProductNotFoundException {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			return updatedQuantity + " item(s) of this product were added to your cart";
		}catch(CustomerNotFoundException ex) {
			return "You must login to continue shopping";
		}catch(ProductNotFoundException ex) {
			return "Cannot find product";
		}
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantity, HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			float subTotal = cartService.updateQuantity(productId, quantity, customer);
			return String.valueOf(subTotal);
		}catch(CustomerNotFoundException ex) {
			return "You must login to continue shopping";
		}catch(ProductNotFoundException ex) {
			return "Cannot find product";
		}
	}
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable(name = "productId") Integer productId,
			HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
			cartService.removeProduct(productId, customer.getId());
			
			return "The product has been removed from your shopping cart.";
			
		} catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No Authenticated Customer");
		}
		return customerService.findByEmail(email);
	}
	
}
