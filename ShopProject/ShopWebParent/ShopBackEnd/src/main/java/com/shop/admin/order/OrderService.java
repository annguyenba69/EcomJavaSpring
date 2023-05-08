package com.shop.admin.order;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Order;

@Service
public class OrderService {
	public static final int ORDERS_PER_PAGE = 7;
	
	@Autowired private OrderRepository orderRepository;
	
	public Page<Order> listByPage(Integer pageNum, String sortDir, String sortField, String keyword){
		Sort sort = Sort.by(sortField);
		sort  = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);
		if(keyword != null) {
			return orderRepository.findAll(keyword, pageable);
		}
		return orderRepository.findAll(pageable);
	}
	
	public Order get(Integer id) throws OrderNotFoundException {
		try {
			return orderRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new OrderNotFoundException("Could not find order with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws OrderNotFoundException {
		long countById = orderRepository.countById(id);
		if(countById == 0) {
			throw new OrderNotFoundException("Could not find order with id: "+ id);
		}
		orderRepository.deleteById(id);
	}
	
	public void save(Order orderInForm) {
		Order orderInDb = orderRepository.findById(orderInForm.getId()).get();
		orderInDb.setPaymentMethod(orderInForm.getPaymentMethod());
		orderInDb.setStatus(orderInForm.getStatus());
		orderInDb.setOrderTracks(orderInForm.getOrderTracks());
		orderRepository.save(orderInDb);
	}
}
