package com.shop.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Brand;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BrandService {
	public static final int BRANDS_PER_PAGE = 7;
	
	@Autowired private BrandRepository brandRepository;
	
	public List<Brand> findAll(){
		return (List<Brand>) brandRepository.findAll();
	}
	
	public Page<Brand> findAll(Integer pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);
		
		if(keyword != null) {
			return brandRepository.findAll(keyword, pageable);
		}
		return brandRepository.findAll(pageable);
	}
	
	public boolean checkUnique(Integer id, String name) {
		Brand existingBrand = brandRepository.findByName(name);
		if(existingBrand == null) return true;
		boolean isCreatingNew = (id == null || id == 0);
		if(isCreatingNew) {
			if(existingBrand != null) {
				return false;
			}
		}else {
			if(existingBrand != null && existingBrand.getId() != id) {
				return false;
			}
		}
		return true;
	}
	
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new BrandNotFoundException("Could not find brand with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws BrandNotFoundException {
		long countById = brandRepository.countById(id);
		if(countById == 0) {
			throw new BrandNotFoundException("Could not find brand with id: "+ id); 
		}
		brandRepository.deleteById(id);
	}
}
