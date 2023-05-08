package com.shop.article;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shop.common.entity.Article;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ArticleRepositoryTests {

	@Autowired ArticleRepository articleRepository;
	
	@Test
	public void testFindNextArticle() {
		Integer id = 6;
		Pageable pageable = PageRequest.of(0, 1);
		List<Article> nextArticle = articleRepository.findNextArticle(id, pageable);
		System.err.println(nextArticle.get(0).getTitle());
	}
	
}
