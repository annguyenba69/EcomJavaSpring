package com.shop.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{

	public Customer findByEmail(String email);
	
	public Customer findByVerificationCode(String verificationCode);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id ,boolean status);
	
}
