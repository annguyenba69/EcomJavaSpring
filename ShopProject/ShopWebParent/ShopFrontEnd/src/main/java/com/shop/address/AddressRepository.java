package com.shop.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Address;
import com.shop.common.entity.Customer;

public interface AddressRepository extends CrudRepository<Address, Integer> {

	
	public List<Address> findByCustomer(Customer customer);
	
	public Address findByIdAndCustomer(Integer id, Customer customer);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = ?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false "
			+ "WHERE a.id != ?1")
	@Modifying
	public void setNonDefaultForOthers(Integer defaultAddressId);
	
	@Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultForShipping = true")
	public Address findDefaultByCustomer(Integer customerId);
}
