package com.shop.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {
	
	@Autowired private BrandRepository brandRepository;
	
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateBrand() {
		Category category = entityManager.find(Category.class, 9);
		Brand brand = new Brand("Acer", "brand.jpg");
		brand.addCategory(category);
		
		Brand savedBrand = brandRepository.save(brand);
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrandWithTwoCategories() {
		Category category1 = entityManager.find(Category.class, 7);
		Category category2 = entityManager.find(Category.class, 9);
		Brand brand = new Brand("HP", "brandhp.jpg");
		brand.addCategory(category1);
		brand.addCategory(category2);
		
		Brand savedBrand = brandRepository.save(brand);
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> iterable =  brandRepository.findAll();
		iterable.forEach(System.out::println);
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testGetById() {
		Brand brand = brandRepository.findById(1).get();
		assertThat(brand).isNotNull();
	}
	
	@Test
	public void testFindAllWithKeywordAndPaging() {
		String keyword = "HP";
		int pageNum = 1;
		int brandPerPage = 5;
		Pageable pageable = PageRequest.of(pageNum - 1, brandPerPage);
		Page<Brand> page = brandRepository.findAll(keyword, pageable);
		List<Brand> brands = page.getContent();
		brands.forEach(System.out::println);
		assertThat(brands.size()).isGreaterThan(0);
	}
}
