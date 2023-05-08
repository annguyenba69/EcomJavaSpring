package com.shop.admin.article;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shop.common.entity.Article;
import com.shop.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticleService {

	public static final int ARTICLES_PER_PAGE = 6;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	
	public Page<Article> listByPage(Integer pageNum, String sortDir, String sortField, String keyword){
		Sort sort = Sort.by(sortField);
		sort  = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, ARTICLES_PER_PAGE, sort);
		if(keyword != null) {
			return articleRepository.findAll(keyword, pageable);
		}
		return articleRepository.findAll(pageable);
	}
	
	public Article get(Integer id) throws ArticleNotFoundException {
		try {
			return articleRepository.findById(id).get();
		}catch(NoSuchElementException ex) {
			throw new ArticleNotFoundException("Could not found Article with id: "+ id);
		}
	}
	
	public void delete(Integer id) throws ArticleNotFoundException {
		long countById = articleRepository.countById(id);
		if(countById == 0) {
			throw new ArticleNotFoundException("Could not found Article with id: "+ id);
		}
		articleRepository.deleteById(id);
	}
	
	public Article save(Article article, User user) {
		if(article.getId() == null) {
			article.setCreatedTime(new Date());
		}
		setDefaultAlias(article);
		article.setUser(user);
		article.setUpdatedTime(new Date());
		return articleRepository.save(article);
	}
	
	private void setDefaultAlias(Article article) {
		if(article.getAlias() == null || article.getAlias().isEmpty()) {
			article.setAlias(article.getTitle().replace(" ", "-"));
		}
	}
	
	public void updatePublishedStatus(Integer id, boolean status) throws ArticleNotFoundException {
		long countById = articleRepository.countById(id);
		if(countById == 0) {
			throw new ArticleNotFoundException("Could not found Article with id: "+id);
		}
		articleRepository.updatedPublishedStatus(id, status);
	}
	
}
