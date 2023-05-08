package com.shop.admin.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer>, PagingAndSortingRepository<Review, Integer> {

	@Query("SELECT r FROM Review r WHERE r.headline LIKE %?1% OR"
			+ " r.product.name LIKE %?1% OR"
			+ " r.customer.firstName LIKE %?1% OR"
			+ " r.customer.lastName LIKE %?1%")
	public Page<Review> findAll(String keyword, Pageable pageable);
	
	public long countById(Integer id);
	
	@Query("UPDATE Review r SET r.enabled = ?2 WHERE r.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
}
