package com.shop.admin.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Country;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CountryRepositoryTests {
	
	@Autowired private CountryRepository countryRepository;
	
	@Test
	public void testCreateCountry() {
		Country country = new Country("VietNam", "VN");
		Country savedCountry = countryRepository.save(country);
		assertThat(savedCountry.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListCountry() {
		Iterable<Country> iterable = countryRepository.findAll();
		iterable.forEach(System.out::println);
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCountry() {
		Country country = countryRepository.findById(1).get();
		String name = "VietNameUpdate";
		country.setName(name);
		Country savedCountry = countryRepository.save(country);
		assertThat(savedCountry.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetCountry() {
		Integer countryId = 1;
		Country country = countryRepository.findById(countryId).get();
		assertThat(country.getId()).isEqualTo(1);
	}
	
	@Test
	public void testDeleteCountry() {
		Integer countryId = 1;
		countryRepository.deleteById(countryId);
		Optional<Country> optional = countryRepository.findById(countryId);
		assertThat(optional.isEmpty());
	}
}
