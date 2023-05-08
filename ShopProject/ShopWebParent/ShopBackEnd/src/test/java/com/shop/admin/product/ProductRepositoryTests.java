package com.shop.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Brand;
import com.shop.common.entity.Category;
import com.shop.common.entity.Product;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired private ProductRepository productRepository;
	
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 4);
		Category category = entityManager.find(Category.class, 7);
		Product product = new Product();
		product.setName("Laptop HP");
		product.setAlias("laptop-hp");
		product.setShortDescription("Short Desc");
		product.setFullDescription("Full Desc");
		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());
		product.setEnabled(true);
		product.setInStock(true);
		product.setPrice(780);
		product.setDiscountPercent(20);
		product.setLength(5);
		product.setWidth(7);
		product.setHeight(4);
		product.setWeight(3);
		product.setMainImage("photo.jpg");
		product.setCategory(category);
		product.setBrand(brand);
		
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterable = productRepository.findAll();
		iterable.forEach(System.out::println);
		assertThat(iterable).size().isGreaterThan(0);
	}
	
	@Test
	public void testGetProduct() {
		Product product = productRepository.findById(1).get();
		System.out.println(product);
		assertThat(product.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateProduct() {
		Product product = productRepository.findById(1).get();
		String newName = "Laptop HP Update";
		product.setName(newName);
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testSaveProductWithImages() {
		Product product = productRepository.findById(1).get();
		
		product.setMainImage("main-image.jpg");
		product.addExtraImage("extra-image1.jpg");
		product.addExtraImage("extra-image2.jpg");
		
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getImages()).size().isEqualTo(2);
	}
	
	@Test
	public void testSaveProductDetail() {
		Product product = productRepository.findById(1).get();
		
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("Os", "Windown 10");
		product.addDetail("Ram", "8GB");
		
		Product savedProduct = productRepository.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
}
