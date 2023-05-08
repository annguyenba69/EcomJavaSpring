package com.shop.admin.country;

import org.springframework.data.repository.CrudRepository;

import com.shop.common.entity.Country;

public interface CountryRepository extends CrudRepository<Country, Integer> {

}
