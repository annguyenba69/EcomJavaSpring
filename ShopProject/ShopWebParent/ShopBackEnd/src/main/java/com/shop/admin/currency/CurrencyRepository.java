package com.shop.admin.currency;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer>{

	public List<Currency> findAllByOrderByNameAsc();
	
}
