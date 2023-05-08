package com.shop.wishlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.common.entity.Wishlist;

public interface WishListRepository extends CrudRepository<Wishlist, Integer>, PagingAndSortingRepository<Wishlist, Integer> {
	
	@Query("SELECT w FROM Wishlist w WHERE w.product.name LIKE %?1% AND w.customer = ?2")
	public Page<Wishlist> findAllByCustomer(String keyword, Customer customer ,Pageable pageable);
	
	@Query("SELECT w FROM Wishlist w WHERE w.customer = ?1")
	public Page<Wishlist> findAllByCustomer(Customer customer ,Pageable pageable);
	
	@Query("SELECT w FROM Wishlist w WHERE w.product.id = ?1 AND w.customer.id = ?2")
	public Wishlist findByProductAndCustomer(Integer productId, Integer customerId);
	
	@Query("DELETE FROM Wishlist w WHERE w.product = ?1 AND w.customer = ?2")
	@Modifying
	public void deleteByProductAndCustomer(Product product, Customer customer);
}
