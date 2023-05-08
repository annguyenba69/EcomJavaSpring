package com.shop.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>, PagingAndSortingRepository<Customer, Integer> {

	@Query("SELECT c FROM Customer c WHERE CONCAT(c.id, ' ', c.firstName, ' ',"
			+ "c.lastName, ' ', c.email, ' ', c.postalCode) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
	
	public long countById(Integer id);
	
	public Customer findByEmail(String email);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean status);
	
}
