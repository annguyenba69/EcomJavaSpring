package com.shop.admin.question;

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
import com.shop.common.entity.Question;
import com.shop.common.entity.User;

import jakarta.persistence.EntityManager;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class QuestionRepositoryTests {

	@Autowired private QuestionRepository questionRepository;
	@Autowired private EntityManager entityManager;
	
	@Test
	public void testCreateFirstQuestion() {
		Product product = entityManager.find(Product.class, 1);
		Customer customer = entityManager.find(Customer.class, 1);
		Question question = new Question();
		question.setQuestionContent("Question Content");
		question.setVotes(4);
		question.setApproved(false);
		question.setAskTime(new Date());
		question.setProduct(product);
		question.setAsker(customer);
		Question savedQuestion = questionRepository.save(question);
		assertThat(savedQuestion.getId()).isGreaterThan(0);
	}
	
	
}
