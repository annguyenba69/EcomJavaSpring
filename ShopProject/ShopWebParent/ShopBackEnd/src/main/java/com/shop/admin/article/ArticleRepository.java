package com.shop.admin.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shop.common.entity.Article;

public interface ArticleRepository extends CrudRepository<Article, Integer>, PagingAndSortingRepository<Article, Integer> {

	@Query("SELECT a FROM Article a")
	public Page<Article> findAll(Pageable pageable);
	
	@Query("SELECT a FROM Article a WHERE a.title LIKE %?1% OR a.content LIKE %?1%")
	public Page<Article> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE Article a SET a.published = ?2 WHERE a.id = ?1")
	@Modifying
	public void updatedPublishedStatus(Integer id, boolean status);
	
	public long countById(Integer id);
}
