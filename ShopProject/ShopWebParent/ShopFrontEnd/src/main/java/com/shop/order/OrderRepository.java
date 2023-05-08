package com.shop.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Customer;
import com.shop.common.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order, Integer> {

	@Query("SELECT o FROM Order o WHERE o.customer = ?1")
	public Page<Order> findByCustomer(Customer customer, Pageable pageable);

}
