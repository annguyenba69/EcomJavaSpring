package com.shop.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {
	
	public Product findByName(String name);
	
	public long countById (Integer id);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
			+ "OR p.alias LIKE %?1%"
			+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"
			+ "OR p.brand.name LIKE %?1%"
			+ "OR p.category.name LIKE %?1%")
	public Page<Product> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
}
