package com.shop.admin.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Country;
import com.shop.common.entity.State;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired private StateRepository stateRepository;
	
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateStateInVietNam() {
		Country vietnam = entityManager.find(Country.class, 2);
		State state = new State("Daklak", vietnam);
		State savedState = stateRepository.save(state);
		assertThat(savedState.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindListStatesByCountry() {
		Country country = entityManager.find(Country.class, 2);
		List<State> states = stateRepository.findByCountryOrderByNameAsc(country);
		states.forEach(System.out::println);
		assertThat(states.size()).isGreaterThan(0);
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId = 1;
		stateRepository.deleteById(stateId);
		Optional<State> optional = stateRepository.findById(stateId);
		assertThat(optional).isEmpty();
	}
	
	@Test
	public void testUpdateState() {
		State state = stateRepository.findById(2).get();
		String name = "Daklak Update";
		state.setName(name);
		State savedState = stateRepository.save(state);
		assertThat(savedState.getName()).isEqualTo(name);
	}
	
	@Test
	public void testGetState() {
		Integer stateId = 2;
		State state = stateRepository.findById(stateId).get();
		assertThat(state).isNotNull();
	}
	
}
