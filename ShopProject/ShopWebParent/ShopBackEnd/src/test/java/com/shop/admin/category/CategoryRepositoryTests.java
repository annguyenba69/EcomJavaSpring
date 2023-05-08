package com.shop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Category;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
	
	@Autowired private CategoryRepository categoryRepository;
	
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Electronic");
		Category savedCategory = categoryRepository.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = entityManager.find(Category.class, 1);
		Category subCategory = new Category("Iphone", parent);
		
		Category savedCategory = categoryRepository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category category = categoryRepository.findById(1).get();
		System.out.println(category);
		for(Category subCategory : category.getChildren()) {
			System.out.println("SubCategory: " + subCategory);
		}
		assertThat(category.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = categoryRepository.findAll();
		
		for(Category category : categories) {
			if(category.getParent() == null) {
				System.out.println(category);
				Set<Category> children = category.getChildren();
				
				for(Category subCategory : children) {
					System.out.println("--/" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for(Category subCategory : children) {
			String name = "";
			for(int i = 0; i < newSubLevel; i ++) {
				name += "--/";
			}
			System.out.println(name + subCategory.getName());
			printChildren(subCategory, newSubLevel);
		}
	}
	
	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(System.out::println);
		assertThat(rootCategories.size()).isGreaterThan(0);
	}
	
	@Test
	public void testFindByName() {
		String categoryName = "Iphone";
		Category category = categoryRepository.findByName(categoryName);
		assertThat(category.getName()).isEqualTo(categoryName);
	}
	
	@Test
	public void testFindByAlias() {
		String aliasName = "Iphone";
		Category category = categoryRepository.findByName(aliasName);
		assertThat(category.getAlias()).isEqualTo(aliasName);
	}
}
