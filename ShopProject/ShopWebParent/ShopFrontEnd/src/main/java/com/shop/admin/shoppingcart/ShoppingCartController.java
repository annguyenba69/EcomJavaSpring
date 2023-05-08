package com.shop.admin.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.Utility;
import com.shop.common.entity.CartItem;
import com.shop.common.entity.Customer;
import com.shop.customer.CustomerNotFoundException;
import com.shop.customer.CustomerService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {
	
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@GetMapping("/shoppingcarts")
	public String index(Model model ,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getCustomer(request);
		List<CartItem> items = cartService.listCartItem(customer);
		float estimatedTotal = 0.0f;
		for(CartItem item : items){
			estimatedTotal += item.subTotal();
		}
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("items", items);
		return "shoppingcarts/shoppingcarts";
	}
	
	private Customer getCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		Customer customer = customerService.findByEmail(email);
		return customer;
	}
}
