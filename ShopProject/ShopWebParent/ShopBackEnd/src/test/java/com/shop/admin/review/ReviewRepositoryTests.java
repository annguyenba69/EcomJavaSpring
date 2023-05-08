package com.shop.admin.review;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Customer;
import com.shop.common.entity.Product;
import com.shop.common.entity.Review;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTests {

	@Autowired private ReviewRepository reviewRepository;
	@Autowired private EntityManager entityManager;
	
	
	@Test
	public void testCreateReview() {
		Product product = entityManager.find(Product.class, 1);
		Customer customer = entityManager.find(Customer.class, 1);
		Review review = new Review();
		review.setHeadline("Product Review");
		review.setComment("Product is so good and beauty");
		review.setRating(4);
		review.setReviewTime(new Date());
		review.setProduct(product);
		review.setEnabled(true);
		review.setCustomer(customer);
		
		Review savedReview = reviewRepository.save(review);
		assertThat(savedReview.getId()).isGreaterThan(0);
	}
	
}
