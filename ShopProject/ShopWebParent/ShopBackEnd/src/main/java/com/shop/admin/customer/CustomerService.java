package com.shop.admin.customer;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Customer;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 7;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Page<Customer> listByPage(Integer pageNum, String sortDir, String sortField, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);
		if (keyword != null) {
			return customerRepository.findAll(keyword, pageable);
		}
		return customerRepository.findAll(pageable);
	}

	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("Could not find customer with: " + id);
		}
	}

	public void save(Customer customer) {
		boolean isUpdatingCustomer = (customer.getId() != null);
		if (isUpdatingCustomer) {
			Customer existingCustomer = customerRepository.findById(customer.getId()).get();
			if (customer.getPassword().isEmpty()) {
				customer.setPassword(existingCustomer.getPassword());
			} else {
				encodedPassword(customer);
			}
		} else {
			encodedPassword(customer);
		}
		customerRepository.save(customer);
	}

	private void encodedPassword(Customer customer) {
		String encodedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(encodedPassword);
	}

	public void delete(Integer id) throws CustomerNotFoundException {
		long countById = customerRepository.countById(id);
		if (countById == 0) {
			throw new CustomerNotFoundException("Could not find customer with id: " + id);
		}
		customerRepository.deleteById(id);
	}

	public boolean isEmailUnique(Integer id ,String email) {
		boolean isCreatingNew = (id == null || id == 0);
		Customer existingCustomer = customerRepository.findByEmail(email);
		if(isCreatingNew) {
			if(existingCustomer != null) {
				return false;
			}
		}else {
			if(existingCustomer != null && existingCustomer.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public void updateEnabledStatus(Integer id, boolean status) throws CustomerNotFoundException {
		long countById = customerRepository.countById(id);
		if (countById == 0) {
			throw new CustomerNotFoundException("Could not find customer with id: " + id);
		}
		customerRepository.updateEnabledStatus(id, status);
	}
}
