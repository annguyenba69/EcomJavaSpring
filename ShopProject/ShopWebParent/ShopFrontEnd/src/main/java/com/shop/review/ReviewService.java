package com.shop.review;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Product;
import com.shop.common.entity.Review;

@Service
public class ReviewService {

	@Autowired private ReviewRepository reviewRepository;
	
	public List<Review> findAllEnabled(Product product){
		return reviewRepository.findAllEnabledByProduct(product);
	}
	
	public Review save(Review review) {
		review.setEnabled(false);
		review.setReviewTime(new Date());
		return reviewRepository.save(review);
	}
}
