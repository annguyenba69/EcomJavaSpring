package com.shop.admin.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.CartItem;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.product.ProductNotFoundException;
import com.shop.product.ProductRepository;
import com.shop.product.ProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired private ProductService productService;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ProductNotFoundException {
		Integer updatedQuantity = quantity;
		Product product = productService.findById(productId);
		CartItem cartItem = shoppingCartRepository.findByCustomerAndProduct(customer, product);
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
		}else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		cartItem.setQuantity(updatedQuantity);
		shoppingCartRepository.save(cartItem);
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItem(Customer customer){
		return shoppingCartRepository.findByCustomer(customer);
	}
	
	public void removeProduct(Integer productId, Integer customerId) {
		shoppingCartRepository.deleteByCustomerAndProduct(customerId, productId);
	}
	
	public void removeAllCartItemsByCustomer(Customer customer) {
		shoppingCartRepository.deleteByCustomer(customer.getId());
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) throws ProductNotFoundException {
		shoppingCartRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productService.findById(productId);
		float subTotal = product.getDiscountPrice() * quantity;
		return subTotal;
	}
	
	public void deleteByCustomer(Customer customer) {
		shoppingCartRepository.deleteByCustomer(customer.getId());
	}
}
