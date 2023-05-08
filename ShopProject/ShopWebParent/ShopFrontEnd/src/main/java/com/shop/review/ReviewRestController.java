package com.shop.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.shop.Helper;
import com.shop.Utility;
import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.common.entity.Review;
import com.shop.customer.CustomerNotFoundException;
import com.shop.product.ProductNotFoundException;
import com.shop.product.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ReviewRestController {
	
	@Autowired private ReviewService reviewService;
	@Autowired private Helper helper;
	@Autowired private ProductService productService;
	@PostMapping("/review/add")
	public String addReview(@Param("headLine") String headLine, @Param("comment") String comment,
			@Param("rating") Integer rating, @Param("productId") Integer productId,HttpServletRequest httpServletRequest) throws CustomerNotFoundException, ProductNotFoundException {
		Customer customer = helper.getCustomer(httpServletRequest);
		Product product = productService.findById(productId);
		Review review = new Review(headLine, comment, rating, product, customer);
		reviewService.save(review);
		return "Success";
	}
}
