package com.shop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Role;
import com.shop.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired private UserRepository userRepository;
	
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userTest1 = new User("test1@gmail.com", "@Admin123", "Firstname 1", "Lastname 1", "phototest1.jpg", true);
		userTest1.addRole(roleAdmin);
		
		User savedUser = userRepository.save(userTest1);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		Role roleSalesperson = entityManager.find(Role.class, 2);
		Role roleEditor = entityManager.find(Role.class, 3);
		User userTest2 = new User("test2@gmail.com", "@Admin123", "Firstname 2", "Lastname 2", "phototest2.jpg", true);
		userTest2.addRole(roleEditor);
		userTest2.addRole(roleSalesperson);
		
		User savedUser = userRepository.save(userTest2);
		assertThat(userTest2.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = userRepository.findAll();
		listUsers.forEach(System.out::println);
		
		assertThat(listUsers).size().isGreaterThan(0);
	}
	
	@Test
	public void testGetUserById() {
		User user = userRepository.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetail() {
		User user = userRepository.findById(1).get();
		String firstnameToUpdate = "FirstName 1 Update";
		user.setFirstName(firstnameToUpdate);
		
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getFirstName()).isEqualTo(firstnameToUpdate);
	}
	
	@Test
	public void testUpdateUserRole() {
		User user = userRepository.findById(1).get();
		Role roleAdmin = entityManager.find(Role.class, 1);
		Role roleEditor = entityManager.find(Role.class, 3);
		user.getRoles().remove(roleAdmin);
		user.addRole(roleEditor);
	
		User savedUser = userRepository.save(user);
		assertThat(savedUser.getRoles().iterator().next()).isEqualTo(roleEditor);
	}
	
	@Test
	public void testDeleteUserById() {
		Integer userId = 2;
		
		userRepository.deleteById(userId);
		Optional<User> user = userRepository.findById(userId);
		assertThat(user).isNotPresent();
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "test1@gmail.com";
		User user = userRepository.findByEmail(email);
		assertThat(user.getEmail()).isEqualTo(email);
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long result = userRepository.countById(id);
		assertThat(result).isEqualTo(1);
	}
	
	@Test
	public void testUpdateEnabledStatus() {
		Integer id = 1;
		userRepository.updateEnabledStatus(false, id);
		User user = userRepository.findById(id).get();
		assertThat(user.isEnabled()).isFalse();
		
	}
	
	@Test
	public void testListByPage() {
		Integer numPerPage = 6;
		Integer pageNum = 1;
		Pageable pageable = PageRequest.of(pageNum - 1, numPerPage);
		Page<User> page = userRepository.findAll(pageable);
		List<User> users = page.getContent();
		users.forEach(System.out::println);
		assertThat(users.size()).isEqualTo(6);
	}
}
