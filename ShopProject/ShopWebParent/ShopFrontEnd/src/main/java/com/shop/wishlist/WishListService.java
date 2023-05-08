package com.shop.wishlist;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.common.entity.Wishlist;
import com.shop.product.ProductNotFoundException;
import com.shop.product.ProductService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WishListService {
	public static final int WISHLISTS_PER_PAGE = 4;
	
	@Autowired private WishListRepository wishListRepository;
	
	@Autowired private ProductService productService;
	
	public Page<Wishlist> findAll(Integer pageNum, String sortField, String sortDir,
			String keyword, Customer customer){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, WISHLISTS_PER_PAGE, sort);
		if(keyword != null) {
			return wishListRepository.findAllByCustomer(keyword, customer, pageable);
		}
		return wishListRepository.findAllByCustomer(customer, pageable);
	}
	
	public String save(Integer productId, Customer customer) throws ProductNotFoundException {
		try {
			Product product = productService.findById(productId);
			if(checkExistingInWishList(product, customer)) {
				return "The product already exists in your wishlist";
			}else {
				Wishlist wishlist = new Wishlist(product, customer, new Date());
				wishListRepository.save(wishlist);
				return "Add products to wishlist successfully";
			}
		}catch(NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not found product with id: "+ productId);
		}
	}
	
	private boolean checkExistingInWishList(Product product, Customer customer) {
		Wishlist existingWishList = wishListRepository.findByProductAndCustomer(product.getId(), customer.getId());
		if(existingWishList != null) {
			return true;
		}
		return false;
	}
	
	public void deleteByProductAndCustomer(Product product, Customer customer) {
		wishListRepository.deleteByProductAndCustomer(product, customer);
	}
	
	
	
}
