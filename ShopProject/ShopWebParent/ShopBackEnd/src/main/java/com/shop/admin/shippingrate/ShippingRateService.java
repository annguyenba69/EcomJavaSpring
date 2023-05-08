package com.shop.admin.shippingrate;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.ShippingRate;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	public static final int SHIPPINGRATES_PER_PAGE = 10;
	
	@Autowired private ShippingRateRepository shippingRateRepository;
	
	public Page<ShippingRate> listByPage(Integer pageNum, String sortField, String sortDir,
			String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, SHIPPINGRATES_PER_PAGE, sort);
		if(keyword != null) {
			return shippingRateRepository.findAll(keyword, pageable);
		}
		return shippingRateRepository.findAll(pageable);
	}
	
	public ShippingRate get(Integer id) throws ShippingRateException {
		try {
			return shippingRateRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new ShippingRateException("Could not found Shipping Rate with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws ShippingRateException {
		long countById = shippingRateRepository.countById(id);
		if(countById == 0) {
			throw new ShippingRateException("Could not found Shipping Rate with id: "+ id);
		}
		shippingRateRepository.deleteById(id);
	}
	
	public void updateCODSupported(Integer id ,boolean status) throws ShippingRateException {
		long countById = shippingRateRepository.countById(id);
		if(countById == 0) {
			throw new ShippingRateException("Could not found Shipping Rate with id: "+ id);
		}
		shippingRateRepository.updateCODSupported(id, status);
	}
	
	public void save(ShippingRate shippingRateInForm) throws ShippingRateAlreadyExistsException {
		boolean isCreatingNew = (shippingRateInForm.getId() == null || shippingRateInForm.getId() == 0);
		ShippingRate existingShippingRate =  shippingRateRepository.findByCountryAndState(shippingRateInForm.getCountry(), shippingRateInForm.getState());
		if(existingShippingRate == null) shippingRateRepository.save(shippingRateInForm);
		if(isCreatingNew) {
			if(existingShippingRate != null) {
				throw new ShippingRateAlreadyExistsException("There's already a rate for distination" + shippingRateInForm.getCountry().getName()+ ", "+shippingRateInForm.getState());
			}else {
				shippingRateRepository.save(shippingRateInForm);
			}
		}else {
			if(existingShippingRate != null && existingShippingRate.getId() != shippingRateInForm.getId()) {
				throw new ShippingRateAlreadyExistsException("There's already a rate for distination" + shippingRateInForm.getCountry().getName()+ ", "+shippingRateInForm.getState());
			}else {
				shippingRateRepository.save(shippingRateInForm);
			}
		}
	}
	
}
