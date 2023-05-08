package com.shop.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shop.common.entity.Category;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

	@MockBean
	private CategoryRepository categoryRepository;
	
	@InjectMocks 
	private CategoryService categoryService;
	
	@Test
	public void testCheckUniqueInNewModelReturnDuplicateName() {
		// 1.Create Mock Data
		Integer id = null;
		String name = "Computers";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		
		// 2.Define behavior of Repo
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(name)).thenReturn(null);
		
		// 3.Call service method
		String result = categoryService.checkUnique(id, name, alias);
		
		// 4.Assert the result
		assertThat(result).isEqualTo("DuplicateName");
	}
	
	@Test
	public void testCheckUniqueInNewModelReturnDuplicateAlias() {
		Integer id = null;
		String name = "Electronics";
		String alias = "electronics";
		
		Category category = new Category(id, name, alias);
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
	}
	
	@Test
	public void testCheckUniqueInNewModelReturnOk() {
		Integer id = null;
		String name = "Electronics";
		String alias = "electronics";
		
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("Ok");
	}
	
	@Test
	public void testCheckUniqueInEditModelReturnDuplicateName() {
		Integer id = 1;
		String name = "Computers";
		String alias = "computers";
		
		Category category = new Category(2, name, alias);
		Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateName");
		
	}
	
	@Test
	public void testCheckUniqueInEditModelReturnDuplicateAlias() {
		Integer id = 1;
		String name = "Computers";
		String alias = "computers";
		
		Category category = new Category(2, name, alias);
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("DuplicateAlias");
		
	}
	
	@Test
	public void testCheckUniqueInEditModelReturnDuplicateOk() {
		Integer id = 1;
		String name = "Computers";
		String alias = "computers";
		
		Category category = new Category(id, name, alias);
		Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
		Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);
		
		String result = categoryService.checkUnique(id, name, alias);
		
		assertThat(result).isEqualTo("Ok");
		
	}
}
