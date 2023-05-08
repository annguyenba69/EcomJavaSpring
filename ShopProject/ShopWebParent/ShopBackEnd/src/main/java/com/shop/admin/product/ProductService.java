package com.shop.admin.product;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 7;
	
	@Autowired private ProductRepository productRepository;
	
	public Page<Product> findAll(Integer pageNum, String sortField, String sortDir,
			String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		if(keyword != null) {
			return productRepository.findAll(keyword, pageable);
		}
		return productRepository.findAll(pageable);
	}
	
	public void saveProductPrice(Product productInForm) {
		Product productInDb = productRepository.findById(productInForm.getId()).get();
		productInDb.setPrice(productInForm.getPrice());
		productInDb.setCost(productInForm.getCost());
		productInDb.setDiscountPercent(productInForm.getDiscountPercent());
		productRepository.save(productInDb);
	}
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		if(product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replace(" ", "-");
			product.setAlias(defaultAlias);
		}else {
			product.setAlias(product.getAlias().replace(" ", "-"));
		}
		product.setUpdatedTime(new Date());
		return productRepository.save(product);
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		long countById = productRepository.countById(id);
		if(countById == 0) {
			throw new ProductNotFoundException("Could not find product with id: "+id);
		}
		productRepository.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find product with id: "+ id);
		}
	}
	
	public void updateEnabledStatus(Integer id, boolean status) throws ProductNotFoundException {
		long countbyId = productRepository.countById(id);
		if(countbyId == 0) {
			throw new ProductNotFoundException("Could not find product with id: "+id);
		}
		productRepository.updateEnabledStatus(id, status);
	}
	
}
