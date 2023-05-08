package com.shop.setting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.Utility;
import com.shop.admin.shoppingcart.ShoppingCartService;
import com.shop.category.CategoryService;
import com.shop.common.entity.CartItem;
import com.shop.common.entity.Category;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Setting;
import com.shop.common.entity.Topic;
import com.shop.customer.CustomerNotFoundException;
import com.shop.customer.CustomerService;
import com.shop.topic.TopicService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SettingFilter implements Filter {
	
	@Autowired private SettingService settingService;
	
	@Autowired private CategoryService categoryService;
	
	@Autowired private ShoppingCartService cartService;
	
	@Autowired private CustomerService customerService;
	
	@Autowired private TopicService topicService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURI().toString();
		
		if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}
	
		
		loadGeneralSettings(servletRequest);
		
		loadCategories(servletRequest);
		
		loadTopics(servletRequest);
		
		loadCart(servletRequest, request);
		
		chain.doFilter(request, response);
	}
	
	private void loadGeneralSettings(ServletRequest request) {
		List<Setting> generalSettings  = settingService.getGeneralSettings();
		for(Setting setting : generalSettings) {
			request.setAttribute(setting.getKey(), setting.getValue());
		}
	}
	
	private void loadCategories(ServletRequest request) {
		List<Category> listParentCategories = categoryService.findParentCategories();
		request.setAttribute("listParentCategories", listParentCategories);
	}
	
	private void loadTopics(ServletRequest request) {
		List<Topic> listTopics = topicService.findAll();
		request.setAttribute("listTopics", listTopics);
	}

	private void loadCart(HttpServletRequest servletRequest, ServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(servletRequest);
		List<CartItem> cartItems = new ArrayList<>();
		float estimatedTotal = 0.0f;
		if(email != null) {
			Customer customer;
			try {
				customer = customerService.findByEmail(email);
				cartItems = cartService.listCartItem(customer);
				for(CartItem item : cartItems){
					estimatedTotal += item.subTotal();
				}
			} catch (CustomerNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		request.setAttribute("estimatedTotal", estimatedTotal);
		request.setAttribute("cartItems", cartItems);
	}
}
