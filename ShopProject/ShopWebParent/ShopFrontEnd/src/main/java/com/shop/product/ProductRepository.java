package com.shop.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shop.common.entity.Category;
import com.shop.common.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "AND p.brand.id IN ?3")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch ,List<Integer> integerSelectedBrand ,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "AND p.price > ?3 "
			+ "AND p.price < ?4")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch , float startPrice, float endPrice,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "AND p.price > ?3")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch , float startPrice,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "AND p.brand.id IN ?3 "
			+ "AND p.price > ?4 "
			+ "AND p.price < ?5")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch ,List<Integer> integerSelectedBrand , float startPrice, float endPrice,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%) "
			+ "AND p.brand.id IN ?3 "
			+ "AND p.price > ?4")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch ,List<Integer> integerSelectedBrand , float startPrice,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true "
			+ "AND (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)")
	public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch,Pageable pageable);
	
	public Product findByAlias(String alias);
	
	public List<Product> findByCategory(Category category);
	
	@Query("SELECT p FROM Product p WHERE p.enabled = true"
			+ " AND size(p.reviews) > 0")
	public List<Product> findProductWithReview();
}
