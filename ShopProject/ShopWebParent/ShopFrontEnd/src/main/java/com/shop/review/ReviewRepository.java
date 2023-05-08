package com.shop.review;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Product;
import com.shop.common.entity.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
	
	@Query("SELECT r FROM Review r WHERE r.product = ?1 AND r.enabled = true")
	public List<Review> findAllEnabledByProduct(Product product);
}
