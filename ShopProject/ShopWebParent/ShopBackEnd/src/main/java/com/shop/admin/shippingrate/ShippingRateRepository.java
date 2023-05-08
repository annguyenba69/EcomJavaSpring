package com.shop.admin.shippingrate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Country;
import com.shop.common.entity.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer>, PagingAndSortingRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %?1% OR sr.state LIKE %?1%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
	@Modifying
	public void updateCODSupported(Integer id, boolean status);
	
	public long countById (Integer id);
	
}
