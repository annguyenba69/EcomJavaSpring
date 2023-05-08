package com.shop.admin.role;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Role;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired private RoleRepository roleRepository;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "Manage EveryThing");
		Role savedRole = roleRepository.save(roleAdmin);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson", "manage product price, "
				+ "customers, shipping, orders and sales report");
		
		Role roleEditor = new Role("Editor", "manage categories, brands, "
				+ "products, articles and menus");
		
		Role roleShipper = new Role("Shipper", "view products, view orders "
				+ "and update order status");
		
		Role roleAssistant = new Role("Assistant", "manage questions and reviews");
		Iterable<Role> savedRoles = roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper,
				roleAssistant));
		assertThat(savedRoles).size().isEqualTo(4);
	}
	
	@Test
	public void testListAllRoles() {
		Iterable<Role> listRoles = roleRepository.findAll();
		listRoles.forEach(System.out::println);
		
		assertThat(listRoles).size().isGreaterThan(0);
	}
	
}
