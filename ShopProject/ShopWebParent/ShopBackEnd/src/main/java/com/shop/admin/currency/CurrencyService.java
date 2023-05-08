package com.shop.admin.currency;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Currency;

@Service
public class CurrencyService {

	@Autowired private CurrencyRepository currencyRepository;
	
	public List<Currency> findAllByOrderByNameAsc(){
		return currencyRepository.findAllByOrderByNameAsc();
	}
	
	public Currency get(Integer id) throws CurrencyNotFoundException{
		try {
			return currencyRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new CurrencyNotFoundException("Could not find currency with id: "+id);
		}
	}
}
