package com.shop.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Article;
import com.shop.common.entity.Topic;

public interface ArticleRepository extends CrudRepository<Article, Integer>, PagingAndSortingRepository<Article, Integer> {
	
	@Query("SELECT a FROM Article a WHERE a.published = true AND a.title LIKE %?1%")
	public Page<Article> findAllWithPublished(String keyword, Pageable pageable);
	
	@Query("SELECT a FROM Article a WHERE a.published = true")
	public Page<Article> findAllWithPublished(Pageable pageable);
	
	@Query("SELECT a FROM Article a WHERE a.topic = ?1")
	public List<Article> findLimitArticleByTopic(Topic topic, Pageable pageable);
	
	@Query("SELECT a FROM Article a WHERE a.alias = ?1 AND a.published = true")
	public Article findByAlias(String alias);

	@Query("SELECT a FROM Article a WHERE a.id > ?1")
	public List<Article> findNextArticle(Integer id, Pageable pageable);
	
	@Query("SELECT a FROM Article a WHERE a.id < ?1")
	public List<Article> findPreviousArticle(Integer id, Pageable pageable);
}

