package com.shop.admin.shippingrate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Country;
import com.shop.common.entity.ShippingRate;

import jakarta.persistence.EntityManager;
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTests {

	@Autowired private ShippingRateRepository shippingRateRepository;
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateNew() {
		Country country = entityManager.find(Country.class, 2);
		ShippingRate shippingRate = new ShippingRate();
		shippingRate.setRate(4.5f);
		shippingRate.setCodSupported(true);
		shippingRate.setDays(4);
		shippingRate.setCountry(country);
		shippingRate.setState("Daklak");
		
		ShippingRate savedShippingRate = shippingRateRepository.save(shippingRate);
		assertThat(savedShippingRate.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdate() {
		ShippingRate shippingRate = shippingRateRepository.findById(1).get();
		shippingRate.setDays(5);
		ShippingRate savedShippingRate = shippingRateRepository.save(shippingRate);
		assertThat(savedShippingRate.getDays()).isEqualTo(5);
	}
	
	@Test
	public void testFindByCountryAndState() {
		Country country = entityManager.find(Country.class, 2);
		ShippingRate shippingRate = shippingRateRepository.findByCountryAndState(country, "Daklak");
		assertThat(shippingRate).isNotNull();
	}
	
	@Test
	public void testFindAll() {
		Iterable<ShippingRate> iterable = shippingRateRepository.findAll();
		iterable.forEach(System.out::println);
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testUpdateCodSupported() {
		ShippingRate shippingRate = shippingRateRepository.findById(1).get();
		shippingRate.setCodSupported(false);
		ShippingRate savedShippingRate = shippingRateRepository.save(shippingRate);
		assertThat(savedShippingRate.isCodSupported()).isFalse();
	}
}
